package dev.kaldiroglu.dp.behavioral.observer.price.solution;

/** A <b>ConcreteObserver</b>: shows the latest price and whether it went up or down. */
public final class Ticker implements PriceListener {

    private String shown = "";

    @Override
    public void priceChanged(PriceChange change) {
        String arrow = change.newPrice() > change.oldPrice() ? "up" : "down";
        shown = change.symbol() + " " + change.newPrice() + " " + arrow;
    }

    public String shown() {
        return shown;
    }
}
