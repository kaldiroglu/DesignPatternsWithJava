package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/**
 * Stage three: <b>the readers poll more often, and act only when the price changed.</b>
 * <p>
 * The best of the three. The feed still knows nobody. The readers poll more often, so they
 * are less late, and they remember the last price, so an unchanged price costs one read
 * and no work: the chart no longer draws the same point again and again.
 * <p>
 * What it cannot fix: a reader sees the price only at the moment it asks. If the price
 * goes from 100 to 106 and back to 100 between two polls, no reader ever sees 106, and the
 * alert at 105 never fires. Polling more often makes this rarer and wastes more reads; it
 * never makes it impossible.
 */
public final class ChangeOnlyReaders {

    private final PriceFeed feed;
    private final Chart chart;
    private final Ticker ticker;
    private final PriceAlert alert;
    private int lastSeen;

    public ChangeOnlyReaders(PriceFeed feed, Chart chart, Ticker ticker, PriceAlert alert) {
        this.feed = feed;
        this.chart = chart;
        this.ticker = ticker;
        this.alert = alert;
        this.lastSeen = feed.price();
    }

    /** Called by a timer, ten times a second. */
    public void poll() {
        int price = feed.price();
        if (price == lastSeen) {
            return;                     // nothing new: one read, no work
        }
        lastSeen = price;
        chart.add(price);
        ticker.show(price);
        alert.check(price);
    }
}
