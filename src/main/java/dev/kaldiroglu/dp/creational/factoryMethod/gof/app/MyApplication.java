package dev.kaldiroglu.dp.creational.factoryMethod.gof.app;

/** GoF's ConcreteCreator: the only class that names MyDocument. */
public class MyApplication extends Application {

    @Override
    public Document createDocument() {
        return new MyDocument();
    }
}
