package dev.kaldiroglu.dp.creational.singleton.threadedLazy;

public class ThreadedLazySingleton {

	private static ThreadedLazySingleton singleton;

	private static int count;
	private String name;

	private ThreadedLazySingleton() {
		name = "ThreadedLazySingleton-" + count;
		count++;
	}

	public static ThreadedLazySingleton getInstance() {
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		}

		if (singleton == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			singleton = new ThreadedLazySingleton();
		}

//		if (singleton == null)
//			singleton = new ThreadedLazySingleton();

		return singleton;
	}

	public static int getInstanceCount() {
		return count;
	}

	public void printName() {
		System.out.println(name);
	}
}
