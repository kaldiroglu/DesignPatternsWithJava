package dev.kaldiroglu.dp.creational.factoryMethod.gof.app;

/** Application.newDocument calls the factory method and opens what it returns. */
public class Main {

    public static void main(String[] args) {
        Application application = new MyApplication();
        application.newDocument();
    }
}
