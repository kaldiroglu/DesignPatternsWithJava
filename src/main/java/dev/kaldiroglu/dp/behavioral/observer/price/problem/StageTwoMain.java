package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/**
 * Stage two: the screens ask the feed once a second. {@link PollingReaders} is a thread, so
 * this class only starts it, changes the price, and stops it. The thread polls at 0, 1 and 2
 * seconds. Most polls find the same price, and the chart draws it again.
 */
public final class StageTwoMain {

    public static void main(String[] args) throws InterruptedException {
        PriceFeed feed = new PriceFeed(100);
        Chart polled = new Chart();
        PollingReaders polling = new PollingReaders(feed, polled, new Ticker(), new PriceAlert(105));
        polling.start();
        Thread.sleep(1_500);
        feed.setPrice(102);
        Thread.sleep(1_000);
        polling.interrupt();
        polling.join();
        System.out.println("Stage two, the screens poll every second: chart " + polled.points());
    }
}
