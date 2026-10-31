
package dev.kaldiroglu.dp.creational.ch04.computer.doubleCheckedLocking;

/**
 * @author akin
 *
 */
public class DoubleCheckedLockingSingleton {
//	private static DoubleCheckedLockingSingleton singleton;
	private static volatile DoubleCheckedLockingSingleton singleton;

	private static int count;
	private String name;

	private DoubleCheckedLockingSingleton() {
		name = "DoubleCheckedLockingSingleton-" + count;
		count++;
	}

	public static DoubleCheckedLockingSingleton getInstance() {
		// Un-synchronized null check is faster.
		if (singleton == null) {
			// Less synchronized area is better.
			synchronized (DoubleCheckedLockingSingleton.class) {
				if (singleton == null) {
					singleton = new DoubleCheckedLockingSingleton();
				}
			}
		}
		return singleton;
	}

	public static int getInstanceCount() {
		return count;
	}

	public void printName() {
		System.out.println(name);
	}
}
