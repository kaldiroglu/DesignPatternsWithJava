package dev.kaldiroglu.dp.creational.singleton.threadSafeLazy;

public class ThreadSafeLazySingletonClient extends Thread {

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			new ThreadSafeLazySingletonClient().start();
		}

		Thread.sleep(5_000);
		System.out.println("Number of objects: " + ThreadSafeLazySingleton.getInstanceCount());
	}

	public void run() {
		ThreadSafeLazySingleton ls = ThreadSafeLazySingleton.getInstance();
		ls.printName();
	}
}
