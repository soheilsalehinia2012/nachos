package nachos.threads;

import java.util.Random;

public class ReaderWriter {
	public static Semaphore noWriting = new Semaphore(1);
	public static Semaphore notAvailable = new Semaphore(1);
	public static Semaphore counterSafeLock = new Semaphore(1);
	
	public static int readersCount = 0;
	public static int sharedMemmory = 0;
	static int prev , current;
	
	public static void write(){
			notAvailable.P();
			noWriting.P();
			notAvailable.V();
			sharedMemmory = new Random().nextInt(100000);
			System.out.println(KThread.currentThread().getName() + " writing " + sharedMemmory );
			noWriting.V();
			ThreadedKernel.alarm.waitUntil(new Random().nextInt(100));
	}
	
	public static void read(){
			notAvailable.P();
			counterSafeLock.P();
			prev = readersCount++;
			if(prev == 0){
				noWriting.P();
			}
			counterSafeLock.V();
			notAvailable.V();
			System.out.println(KThread.currentThread().getName() +" Reading: " + sharedMemmory);
			counterSafeLock.P();
			current = --readersCount;
			if(current == 0){
				noWriting.V();
			}
			counterSafeLock.V();
	}
	
	public static void selftest(){
		
		
		KThread a = new KThread(new Runnable() {
			public void run() {
				write();
				read();
				read();
				write();
			}
		}).setName("a");
		KThread b = new KThread(new Runnable() {
			public void run() {
				read();
				write();
				write();
			}
		}).setName("b");
		KThread c = new KThread(new Runnable() {
			public void run() {
				write();
				read();
				write();
			}
		}).setName("c");
		KThread d = new KThread(new Runnable() {
			public void run() {
				write();
				read();
				write();
				read();
				write();
			}
		}).setName("d");
		KThread e = new KThread(new Runnable() {
			public void run() {
				read();
				read();
				write();
				read();
				write();
			}
		}).setName("e");
		KThread f = new KThread(new Runnable() {
			public void run() {
				read();
			}
		}).setName("f");
		
		
		a.fork();
		b.fork();
		c.fork();
		d.fork();
		e.fork();
		f.fork();
		
	}
}
