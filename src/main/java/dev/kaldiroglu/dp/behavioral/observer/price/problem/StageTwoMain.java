package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/**
 * Stage two: the screens ask the feed once a second. Most polls find the same price, and the
 * chart draws it again.
 */
public final class StageTwoMain {

    public static void main(String[] args) {
        PriceFeed feed = new PriceFeed(100);
        Chart polled = new Chart();
        PollingReaders polling = new PollingReaders(feed, polled, new Ticker(), new PriceAlert(105));
        polling.poll();
        polling.poll();
        feed.setPrice(102);
        polling.poll();
        System.out.println("Stage two, the screens poll every second: chart " + polled.points());
    }
}
