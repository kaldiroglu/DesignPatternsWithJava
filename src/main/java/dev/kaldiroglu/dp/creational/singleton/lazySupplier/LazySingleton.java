package dev.kaldiroglu.dp.creational.singleton.lazySupplier;

public class LazySingleton {
    private static Lazy<LazySingleton> singleton = new Lazy<>(LazySingleton::new);

    private LazySingleton(){}

    public static LazySingleton getInstance() {
        return singleton.getValue();
    }
}
