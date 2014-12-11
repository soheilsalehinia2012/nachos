package nachos.threads;


public class MyTestjava {
	
	public MyTestjava() {
		// TODO Auto-generated constructor stub
	}
	
	public static void selfTest() {
    	//System.out.println("1");
		KThread thread1 = new KThread(new Runnable() {
			public void run() {
				System.out.println("MyTestjava SelfTest()");
			}
		});
		//System.out.println("2");
		thread1.fork();
		//System.out.println("3");
    }
	
}
