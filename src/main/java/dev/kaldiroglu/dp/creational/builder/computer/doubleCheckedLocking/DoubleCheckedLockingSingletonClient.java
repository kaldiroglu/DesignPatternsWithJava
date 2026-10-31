
package dev.kaldiroglu.dp.creational.ch04.computer.doubleCheckedLocking;

/**
 * @author akin
 *
 */
public class DoubleCheckedLockingSingletonClient extends Thread {

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 100; i++) {
			new DoubleCheckedLockingSingletonClient().start();
		}

		Thread.sleep(3_000);
		System.out.println("Number of objects: " + DoubleCheckedLockingSingleton.getInstanceCount());

	}

	public void run() {
		DoubleCheckedLockingSingleton ls = DoubleCheckedLockingSingleton.getInstance();
		ls.printName();
	}
}
