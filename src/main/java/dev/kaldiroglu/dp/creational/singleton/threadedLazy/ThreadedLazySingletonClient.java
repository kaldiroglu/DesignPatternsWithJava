package dev.kaldiroglu.dp.creational.singleton.threadedLazy;

public class ThreadedLazySingletonClient extends Thread {

	public static void main(String[] args) throws InterruptedException {
		for(int i = 0; i < 100; i++){
			new ThreadedLazySingletonClient().start();
		}
		Thread.sleep(5000);
		System.out.println("Number of objects: " + ThreadedLazySingleton.getInstanceCount());
	}
	
	public void run(){
		ThreadedLazySingleton ls = ThreadedLazySingleton.getInstance();
		ls.printName();
	}
}
