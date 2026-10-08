package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/** A reader of the price: shows only the latest one. */
public final class Ticker {

    private int shown;

    public void show(int price) {
        shown = price;
    }

    public int shown() {
        return shown;
    }
}
