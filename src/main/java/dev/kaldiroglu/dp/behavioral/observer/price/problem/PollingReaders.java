package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/**
 * Stage two: <b>the readers poll the feed.</b>
 * <p>
 * A real improvement on stage one: the feed knows nobody, and a new reader needs no change
 * to the feed. This class is a thread: it wakes up every second, and each reader asks for
 * the price. A new reader is still an edit to this class.
 * <p>
 * What it costs: a change is seen only at the next poll, so readers are late; most polls
 * find the same price; and a change that is undone before the next poll is never seen.
 */
public final class PollingReaders extends Thread {

    private static final long ONE_SECOND = 1_000;

    private final PriceFeed feed;
    private final Chart chart;
    private final Ticker ticker;
    private final PriceAlert alert;

    public PollingReaders(PriceFeed feed, Chart chart, Ticker ticker, PriceAlert alert) {
        this.feed = feed;
        this.chart = chart;
        this.ticker = ticker;
        this.alert = alert;
    }

    /** Polls once a second, until the thread is interrupted. */
    @Override
    public void run() {
        try {
            while (true) {
                poll();
                Thread.sleep(ONE_SECOND);
            }
        } catch (InterruptedException stopped) {
            // the thread was asked to stop
        }
    }

    public void poll() {
        chart.add(feed.price());
        ticker.show(feed.price());
        alert.check(feed.price());
    }
}
