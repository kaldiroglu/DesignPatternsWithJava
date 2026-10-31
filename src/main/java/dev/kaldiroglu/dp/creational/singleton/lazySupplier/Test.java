package dev.kaldiroglu.dp.creational.singleton.lazySupplier;

public class Test {
    public static void main(String[] args) {
        Lazy<String> lazyString = new Lazy<>(() -> "Hello, World!");
        System.out.println("Value created: " + lazyString.isValueCreated());
        System.out.println("Value: " + lazyString.getValue());
        System.out.println("Value created: " + lazyString.isValueCreated());

        LazySingleton lazySingleton = LazySingleton.getInstance();
    }
}
