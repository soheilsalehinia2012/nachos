package nachos.threads;

import java.util.LinkedList;

import nachos.machine.*;

/**
 * An implementation of condition variables that disables interrupt()s for
 * synchronization.
 *
 * <p>
 * You must implement this.
 *
 * @see	nachos.threads.Condition
 */
public class Condition2 {
    /**
     * Allocate a new condition variable.
     *
     * @param	conditionLock	the lock associated with this condition
     *				variable. The current thread must hold this
     *				lock whenever it uses <tt>sleep()</tt>,
     *				<tt>wake()</tt>, or <tt>wakeAll()</tt>.
     */
    public Condition2(Lock conditionLock) {
	this.conditionLock = conditionLock;
	
    }

    /**
     * Atomically release the associated lock and go to sleep on this condition
     * variable until another thread wakes it using <tt>wake()</tt>. The
     * current thread must hold the associated lock. The thread will
     * automatically reacquire the lock before <tt>sleep()</tt> returns.
     */
    public void sleep() {
	Lib.assertTrue(conditionLock.isHeldByCurrentThread());
	conditionLock.release();
	boolean status = Machine.interrupt().disable();
	waitQueue.add(KThread.currentThread());
	KThread.sleep();
	Machine.interrupt().restore(status);
	conditionLock.acquire();
	
    }

    /**
     * Wake up at most one thread sleeping on this condition variable. The
     * current thread must hold the associated lock.
     */
    public void wake() {
	Lib.assertTrue(conditionLock.isHeldByCurrentThread());
	
	if (!waitQueue.isEmpty()){
		boolean status = Machine.interrupt().disable();
		KThread thread = waitQueue.removeFirst();
		
		if(thread != null){
			thread.ready();
		}
		Machine.interrupt().restore(status);
    	}
    }
    /**
     * Wake up all threads sleeping on this condition variable. The current
     * thread must hold the associated lock.
     */
    
    public void wakeAll() {
	Lib.assertTrue(conditionLock.isHeldByCurrentThread());
		while (!waitQueue.isEmpty()){
				wake();
    		}
    }
    
    
    public static void selfTest() {
    	Lib.debug(dbgThread, "Enter Condition2.selfTest");
    	
    	Lock lock = new Lock();
    	final Condition2 c2 = new Condition2(lock);
    	KThread t1 = new KThread(
    		new Runnable() {
				public void run() {
					//c2.conditionLock.acquire();
					for(int i=0 ; i<500 ; i++){
						c2.conditionLock.acquire();
						System.out.println(c2.x);
						c2.x++;
						if(c2.x == 50){
							System.out.println("wake");
							c2.wake();
						}
						c2.conditionLock.release();
					}
					
					
				}
			}
    	);
    	
    	
    	KThread t2 = new KThread(new Runnable() {
			public void run() {
				//System.out.println("run2");
				if(c2.x < 50){
					c2.conditionLock.acquire();
					System.out.println("sleep");
					c2.sleep();
		    		c2.conditionLock.release();
				}
			}
		});
    	t1.fork();
    	t2.fork();
    	
    }

    int x = 0;
    private Lock conditionLock;
    private static final char dbgThread = 't';
    private LinkedList<KThread> waitQueue = new LinkedList<KThread>();
}
