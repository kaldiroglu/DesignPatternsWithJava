package dev.kaldiroglu.dp.creational.factoryMethod.gof.app;

/** GoF's ConcreteProduct. */
public class MyDocument implements Document {

    @Override
    public void open() {
        System.out.println("MyDocument is open");
    }

    @Override
    public void close() {
    }

    @Override
    public void save() {
    }

    @Override
    public void revert() {
    }
}
