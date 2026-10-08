package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/**
 * A reader of the price: fires once when the price reaches its limit.
 * <p>
 * The promise of the story: an alert at 105 fires whenever the price reaches 105.
 */
public final class PriceAlert {

    private final int limit;
    private boolean fired;

    public PriceAlert(int limit) {
        this.limit = limit;
    }

    public void check(int price) {
        if (price >= limit) {
            fired = true;
        }
    }

    public boolean fired() {
        return fired;
    }
}
