package dev.kaldiroglu.dp.creational.singleton.pugh;

/**
 * Created by Murat Öngüdü on 4/4/2015.
 */
public class BillPughSingletonClient extends Thread {

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            new BillPughSingletonClient().start();
        }

        System.out.println("Number of objects: " + BillPughSingleton.getInstanceCount());
    }

    public void run() {
        BillPughSingleton ls = BillPughSingleton.getInstance();
        ls.printName();
    }
}
