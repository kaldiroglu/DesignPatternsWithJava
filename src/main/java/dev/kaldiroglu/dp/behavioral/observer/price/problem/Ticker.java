package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/** A reader of the price: shows only the latest one. */
public final class Ticker {

    private volatile int shown;     // set by the polling thread, read by main

    public void show(int price) {
        shown = price;
    }

    public int shown() {
        return shown;
    }
}
