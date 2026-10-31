/*
 * All rights reserved
 * Written by Akin Kaldiroglu for Design Patterns Seminar
 * 27 May 2009
 * akink@bilginc.com
 */

package dev.kaldiroglu.dp.creational.singleton.threadSafeLazy;

public class ThreadSafeLazySingleton {

    private static volatile ThreadSafeLazySingleton singleton;

    private static int count;
    private String name;

    private ThreadSafeLazySingleton() {
        name = "ThreadSafeLazySingleton" + count;
        count++;
    }

    public static ThreadSafeLazySingleton getInstance() {
        synchronized (ThreadSafeLazySingleton.class) {
            if (singleton == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                singleton = new ThreadSafeLazySingleton();
            }
        }
        return singleton;
    }

    public void printName() {
        System.out.println(name);
    }

    public static int getInstanceCount() {
        return count;
    }
}
