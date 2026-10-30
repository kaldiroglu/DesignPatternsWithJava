package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/**
 * Stage three: the screens ask the feed ten times a second and act only on a change.
 * {@link ChangeOnlyReaders} is a thread, so this class only starts it, changes the price
 * twice, and stops it. Only the two changes reach the chart.
 */
public final class StageThreeMain {

    public static void main(String[] args) throws InterruptedException {
        PriceFeed feed = new PriceFeed(100);
        Chart changes = new Chart();
        ChangeOnlyReaders readers = new ChangeOnlyReaders(feed, changes, new Ticker(), new PriceAlert(105));
        readers.start();
        Thread.sleep(250);
        feed.setPrice(102);
        Thread.sleep(300);
        feed.setPrice(103);
        Thread.sleep(250);
        readers.interrupt();
        readers.join();
        System.out.println("Stage three, the screens poll ten times a second: chart " + changes.points());
    }
}
