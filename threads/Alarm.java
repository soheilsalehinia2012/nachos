package nachos.threads;

import nachos.machine.*;

import java.util.LinkedList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Uses the hardware timer to provide preemption, and to allow threads to sleep
 * until a certain time.
 */
public class Alarm {
    /**
     * Allocate a new Alarm. Set the machine's timer interrupt handler to this
     * alarm's callback.
     *
     * <p><b>Note</b>: Nachos will not function correctly with more than one
     * alarm.
     */
    public Alarm() {
	Machine.timer().setInterruptHandler(new Runnable() {
		public void run() { timerInterrupt(); }
	    });
    }

    /**
     * The timer interrupt handler. This is called by the machine's timer
     * periodically (approximately every 500 clock ticks). Causes the current
     * thread to yield, forcing a context switch if there is another thread
     * that should be run.
     */
    public void timerInterrupt() {
	//KThread.currentThread().yield();
    	for(int i = waitQueue.size()-1; i>=0;i--){
    		if(Machine.timer().getTime() >= waitQueue.get(i).time ){
    			KThread temp = waitQueue.get(i).thread;
    			temp.ready();
    			waitQueue.remove(i);
    		}
    	}
    	KThread.yield();
    }
    
    public static void selfTest() {
    	System.out.println("hello");
    	ThreadedKernel.alarm.waitUntil(10000000);
    	System.out.println("bye");
    }

    /**
     * Put the current thread to sleep for at least <i>x</i> ticks,
     * waking it up in the timer interrupt handler. The thread must be
     * woken up (placed in the scheduler ready set) during the first timer
     * interrupt where
     *
     * <p><blockquote>
     * (current time) >= (WaitUntil called time)+(x)
     * </blockquote>
     *
     * @param	x	the minimum number of clock ticks to wait.
     *
     * @see	nachos.machine.Timer#getTime()
     */
    LinkedList<ThreadInfo> waitQueue = new LinkedList<ThreadInfo>();
    
    public void waitUntil(long x) {
	// for now, cheat just to get something working (busy waiting is bad)
    	//long wakeTime = Machine.timer().getTime() + x;
    	//while (wakeTime > Machine.timer().getTime())
    		//KThread.yield();
    	Machine.interrupt().disable();
    	ThreadInfo TT = new ThreadInfo (KThread.currentThread(), Machine.timer().getTime() + x);
	    waitQueue.add(TT);
	    TT.thread.issleep = true;
	    KThread.sleep();
	    Machine.interrupt().enable();
    }
    
    class ThreadInfo {
    	public KThread thread;
    	public long time;
    	
    	public ThreadInfo (KThread thread,long wakeTime){
    		this.thread =thread;
    		this.time = wakeTime;
    	}
    }
}
