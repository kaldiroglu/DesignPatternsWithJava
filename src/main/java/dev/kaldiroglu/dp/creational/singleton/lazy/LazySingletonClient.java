package dev.kaldiroglu.dp.creational.singleton.lazy;

public class LazySingletonClient {

	public static void main(String[] args) {
		for (int i = 0; i<100; i++){
			LazySingleton s = LazySingleton.getInstance();
			s.printName();
		}
		System.out.println("Number of objects: " + LazySingleton.getInstanceCount());
	}
}
