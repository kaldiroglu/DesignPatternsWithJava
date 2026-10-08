package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/**
 * Stage one: <b>the feed calls every reader itself.</b>
 * <p>
 * When the price changes, the feed tells the chart, the ticker and the alert at once. It
 * works, and no change is ever missed. What it costs:
 * <ul>
 *   <li>The feed knows every reader by class. A fourth reader is an edit to the feed.</li>
 *   <li>A reader cannot stop reading while the program runs; it is a field of the feed.</li>
 *   <li>The feed cannot be reused, or tested, without all three readers.</li>
 * </ul>
 */
public final class DirectPriceFeed {

    private final Chart chart;
    private final Ticker ticker;
    private final PriceAlert alert;
    private int price;

    public DirectPriceFeed(Chart chart, Ticker ticker, PriceAlert alert) {
        this.chart = chart;
        this.ticker = ticker;
        this.alert = alert;
    }

    public void setPrice(int price) {
        this.price = price;
        chart.add(price);
        ticker.show(price);
        alert.check(price);
    }

    public int price() {
        return price;
    }
}
