package dev.kaldiroglu.dp.creational.singleton.gof;

public class Singleton {

    private static Singleton uniqueInstance;
    private String singletonData;

    private Singleton(){}

    public static Singleton instance(){
        return uniqueInstance;
    }

    public void singletonOperation(){
         System.out.println("Performing an operation on the singleton instance.");
    }

    public String getSingletonData(){
         return singletonData;
    }
}
