package dev.kaldiroglu.dp.behavioral.observer.price.problem;

/**
 * Stage three meets a fast spike. The screens poll ten times a second and act only on a
 * change, but the price goes to 106 and back to 100 between two polls, so the alert at 105
 * never fires.
 */
public final class FastSpikeMain {

    public static void main(String[] args) {
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
