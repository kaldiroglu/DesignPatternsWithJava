package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/**
 * Stage three meets a fast spike. The screens poll ten times a second and act only on a
 * change, but the price goes to 106 and back to 100 between two polls, so the alert at 105
 * never fires. This class calls poll itself, without the thread, so that the spike always
 * falls between the same two polls.
 */
public final class FastSpikeMain {

    public static void main(String[] args) {
        PriceFeed fast = new PriceFeed(100);
        Chart chart = new Chart();
        Ticker ticker = new Ticker();
        PriceAlert missed = new PriceAlert(105);
        ChangeOnlyReaders readers = new ChangeOnlyReaders(fast, chart, ticker, missed);
        int readsBefore = fast.reads();
        for (int tenth = 1; tenth <= 100; tenth++) {
            if (tenth == 20) fast.setPrice(102);
            if (tenth == 55) { fast.setPrice(106); fast.setPrice(100); }   // a spike between polls
            if (tenth == 80) fast.setPrice(103);
            readers.poll();
            if (tenth == 20 || tenth == 55 || tenth == 80) {
                System.out.println("Stage three, ten polls a second, poll " + tenth + ":\nchart " + chart.points()
                        + "\nticker shows " + ticker.shown() + "\nalert fired: " + missed.fired() + "\n");
            }
        }
        System.out.println("Reads in ten seconds: " + (fast.reads() - readsBefore));
    }
}
