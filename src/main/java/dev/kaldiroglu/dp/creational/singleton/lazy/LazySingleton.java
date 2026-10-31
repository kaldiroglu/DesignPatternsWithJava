package dev.kaldiroglu.dp.creational.singleton.lazy;

/**
 * Singleton class that loads the singleton object lazily.
 * @author akin
 *
 */
public class LazySingleton {

	private static LazySingleton singleton;
	
	private static int count;
	private String name;

	private LazySingleton() {
		name = "LazySingleton-" + count;
		count++;
	}

	public static LazySingleton getInstance() {
		if(singleton == null)
			singleton = new LazySingleton();
		return singleton;
	}

	public void printName() {
		System.out.println(name);
	}

	public static int getInstanceCount() {
		return count;
	}
}
