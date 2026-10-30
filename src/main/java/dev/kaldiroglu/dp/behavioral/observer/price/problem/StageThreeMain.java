package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/**
 * Stage three: the screens ask the feed ten times a second and act only on a change.
 * {@link ChangeOnlyReaders} is a thread, so this class only starts it, changes the price
 * twice, and stops it. Only the two changes reach the chart.
 */
public final class StageThreeMain {

    public static void main(String[] args) throws InterruptedException {
        PriceFeed feed = new PriceFeed(100);
        Chart chart = new Chart();
        Ticker ticker = new Ticker();
        PriceAlert alert = new PriceAlert(105);
        ChangeOnlyReaders readers = new ChangeOnlyReaders(feed, chart, ticker, alert);
        readers.start();
        Thread.sleep(250);
        feed.setPrice(102);
        Thread.sleep(300);
        print(chart, ticker, alert);
        feed.setPrice(103);
        Thread.sleep(300);
        print(chart, ticker, alert);
        readers.interrupt();
        readers.join();
    }

    private static void print(Chart chart, Ticker ticker, PriceAlert alert) {
        System.out.println("Stage three, the screens poll ten times a second:\nchart " + chart.points() + "\nticker shows "
                + ticker.shown() + "\nalert fired: " + alert.fired() + "\n");
    }
}
