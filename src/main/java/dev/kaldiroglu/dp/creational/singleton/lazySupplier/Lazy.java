package dev.kaldiroglu.dp.creational.singleton.lazySupplier;

import java.util.function.Supplier;

public class Lazy<T> {
    private final Supplier<T> supplier;
    private volatile T value; // The "magic" volatile keyword

    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T getValue() {
        T result = value;
        if (result == null) {
            synchronized (this) {
                result = value;
                if (result == null) {
                    value = result = supplier.get();
                }
            }
        }
        return result;
    }

    public boolean isValueCreated() {
        return value != null;
    }
}
