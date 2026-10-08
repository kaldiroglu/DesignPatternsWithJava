package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/**
 * Stages two and three: <b>a feed that knows nobody.</b>
 * <p>
 * It only holds the price. Readers ask for it when they want it — they poll. It also
 * counts how often it is asked, to show how much of the polling finds nothing new.
 */
public final class PriceFeed {

    private int price;
    private int reads;

    public PriceFeed(int price) {
        this.price = price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int price() {
        reads++;
        return price;
    }

    public int reads() {
        return reads;
    }
}
