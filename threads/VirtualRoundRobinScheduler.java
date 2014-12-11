package nachos.threads;

import nachos.machine.*;

import java.util.LinkedList;
import java.util.Iterator;
import java.util.Random;

/**
 * A round-robin scheduler tracks waiting threads in FIFO queues, implemented
 * with linked lists. When a thread begins waiting for access, it is appended
 * to the end of a list. The next thread to receive access is always the first
 * thread in the list. This causes access to be given on a first-come
 * first-serve basis.
 */
public class VirtualRoundRobinScheduler extends Scheduler {
    /**
     * Allocate a new round-robin scheduler.
     */
    public VirtualRoundRobinScheduler() {
    }
    
    /**
     * Allocate a new FIFO thread queue.
     *
     * @param	transferPriority	ignored. Round robin schedulers have
     *					no priority.
     * @return	a new FIFO thread queue.
     */
    public ThreadQueue newThreadQueue(boolean transferPriority) {
	return new FifoQueue();
    }
    
    public static void selftest(){
		KThread t1 = new KThread(new Runnable() {
			public void run() {
				//System.out.println("t1");
				for(int i=0 ; i<10 ; i++){
					System.out.println("t1 is sleeped");
					ThreadedKernel.alarm.waitUntil(new Random().nextInt(1000));
					System.out.println("t1 is woke up");
				}
			}
		});
		KThread t2 = new KThread(new Runnable() {
			public void run() {
				//System.out.println("t2");
				for(int i=0 ; i<10 ; i++){
					System.out.println("t2 is sleeped");
					ThreadedKernel.alarm.waitUntil(new Random().nextInt(1000));
					System.out.println("t2 is woke up");
				}
			}
		});
		KThread t3 = new KThread(new Runnable() {
			public void run() {
				//System.out.println("t3");
				for(int i=0 ; i<10 ; i++){
					System.out.println("t3 is sleeped");
					ThreadedKernel.alarm.waitUntil(new Random().nextInt(1000));
					System.out.println("t3 is woke up");
				}
			}
		});
		KThread t4 = new KThread(new Runnable() {
			public void run() {
				//System.out.println("t4");
				for(int i=0 ; i<10 ; i++){
					System.out.println("t4 is sleeped");
					ThreadedKernel.alarm.waitUntil(new Random().nextInt(1000));
					System.out.println("t4 is woke up");
				}
			}
		});
		t1.fork();
		t2.fork();
		t3.fork();
		t4.fork();
	}

    private class FifoQueue extends ThreadQueue {
	/**
	 * Add a thread to the end of the wait queue.
	 *
	 * @param	thread	the thread to append to the queue.
	 */    
	public void waitForAccess(KThread thread) {
	    Lib.assertTrue(Machine.interrupt().disabled());
		       
	    if (thread.issleep){
	    	waitQueueVirtual.add(thread);
	    	thread.issleep = false;
	    }
	    else{
	    	waitQueue.add(thread);
	    }
	}

	/**
	 * Remove a thread from the beginning of the queue.
	 *
	 * @return	the first thread on the queue, or <tt>null</tt> if the
	 *	       	queue is
	 *		empty.
	 */
	public KThread nextThread() {
	    Lib.assertTrue(Machine.interrupt().disabled());
		       
	    if(!waitQueueVirtual.isEmpty()){
	    
	    	return (KThread) waitQueueVirtual.removeFirst();
	    }
	    
	    if (waitQueue.isEmpty())
		return null;

	    //System.out.println("Q");
	    return (KThread) waitQueue.removeFirst();
	}

	/**
	 * The specified thread has received exclusive access, without using
	 * <tt>waitForAccess()</tt> or <tt>nextThread()</tt>. Assert that no
	 * threads are waiting for access.
	 */
	public void acquire(KThread thread) {
	    Lib.assertTrue(Machine.interrupt().disabled());
		       
	    Lib.assertTrue(waitQueue.isEmpty());
	}

	/**
	 * Print out the contents of the queue.
	 */
	public void print() {
	    Lib.assertTrue(Machine.interrupt().disabled());

	    for (Iterator i=waitQueue.iterator(); i.hasNext(); )
		System.out.print((KThread) i.next() + " ");
	}
	
	

	private LinkedList<KThread> waitQueue = new LinkedList<KThread>();
	private LinkedList<KThread> waitQueueVirtual = new LinkedList<KThread>();
    }
}
