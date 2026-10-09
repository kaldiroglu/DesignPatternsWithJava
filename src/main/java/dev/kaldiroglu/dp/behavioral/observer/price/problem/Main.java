package dev.kaldiroglu.dp.behavioral.observer.price.problem;

import java.util.List;

/**
 * Runs the three stages. Stage one sees every price but knows every screen; stage three
 * polls ten times a second and misses a spike to 106, so the alert at 105 never fires.
 */
public final class Main {

    public static void main(String[] args) {
        Chart chart = new Chart();
        PriceAlert alert = new PriceAlert(105);
        DirectPriceFeed direct = new DirectPriceFeed(chart, new Ticker(), alert);
        for (int price : List.of(102, 106, 100, 103)) {
            direct.setPrice(price);
        }
        System.out.println("Stage one, the feed calls every screen: chart " + chart.points()
                + ", alert fired: " + alert.fired());

        PriceFeed feed = new PriceFeed(100);
        Chart polled = new Chart();
        PollingReaders polling = new PollingReaders(feed, polled, new Ticker(), new PriceAlert(105));
        polling.poll();
        polling.poll();
        feed.setPrice(102);
        polling.poll();
        System.out.println("Stage two, the screens poll every second: chart " + polled.points());

        PriceFeed fast = new PriceFeed(100);
        Chart changes = new Chart();
        PriceAlert missed = new PriceAlert(105);
        ChangeOnlyReaders readers = new ChangeOnlyReaders(fast, changes, new Ticker(), missed);
        int readsBefore = fast.reads();
        for (int tenth = 1; tenth <= 100; tenth++) {
            if (tenth == 20) fast.setPrice(102);
            if (tenth == 55) { fast.setPrice(106); fast.setPrice(100); }   // a spike between polls
            if (tenth == 80) fast.setPrice(103);
            readers.poll();
        }
        System.out.println("Stage three, ten polls a second for ten seconds: chart " + changes.points()
                + ", alert fired: " + missed.fired() + ", reads " + (fast.reads() - readsBefore));
    }
}
