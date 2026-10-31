package dev.kaldiroglu.dp.creational.singleton.pugh;

/**
 * inspired by http://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-with-examples
 *
 * Created by Murat Öngüdü on 4/4/2015.
 */
public class BillPughSingleton {

    private static int count;
    private String name;

    private BillPughSingleton() {
        name = "BillPughSingleton " + count;
        count++;
    }

    public String getName() {
        return name;
    }

    private static class SingletonHelper{
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance(){
        return SingletonHelper.INSTANCE;
    }

    public void printName() {
        System.out.println(name); ;
    }

    public static int getInstanceCount(){
        return count;
    }
}