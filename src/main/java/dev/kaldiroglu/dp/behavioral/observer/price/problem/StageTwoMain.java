package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/**
 * Stage two: the screens ask the feed once a second. {@link PollingReaders} is a thread, so
 * this class only starts it, changes the price, and stops it. The thread polls at 0, 1 and 2
 * seconds, and this class prints what every screen has half a second after each poll. Most polls find the same price, and the chart draws it again.
 */
public final class StageTwoMain {

    public static void main(String[] args) throws InterruptedException {
        PriceFeed feed = new PriceFeed(100);
        Chart chart = new Chart();
        Ticker ticker = new Ticker();
        PriceAlert alert = new PriceAlert(105);
        PollingReaders polling = new PollingReaders(feed, chart, ticker, alert);
        polling.start();
        Thread.sleep(500);
        print(chart, ticker, alert);
        Thread.sleep(1_000);
        print(chart, ticker, alert);
//        feed.setPrice(120);
//        Thread.sleep(1);
//        print(chart, ticker, alert);
//        feed.setPrice(110);
//        Thread.sleep(1_00);
//        print(chart, ticker, alert);
        polling.interrupt();
        polling.join();
    }

    private static void print(Chart chart, Ticker ticker, PriceAlert alert) {
        System.out.println("Stage two, the screens poll every second:\nchart " + chart.points() + "\nticker shows "
                + ticker.shown() + "\nalert fired: " + alert.fired() + "\n");
    }
}
