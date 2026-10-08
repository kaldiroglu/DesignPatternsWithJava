package dev.kaldiroglu.dp.behavioral.observer.price.solution;

import dev.kaldiroglu.dp.behavioral.observer.price.problem.ChangeOnlyReaders;

import java.util.ArrayList;
import java.util.List;

/**
 * The same ten seconds of prices, read by stage three's polling readers and by listeners.
 * The price spikes from 100 to 106 and back to 100 between two polls.
 */
public final class Main {

    public static void main(String[] args) {
        // Stage three: readers poll ten times a second (100 polls in ten seconds).
        var feed = new dev.kaldiroglu.dp.behavioral.observer.price.problem.PriceFeed(100);
        var chart = new dev.kaldiroglu.dp.behavioral.observer.price.problem.Chart();
        var ticker = new dev.kaldiroglu.dp.behavioral.observer.price.problem.Ticker();
        var alert = new dev.kaldiroglu.dp.behavioral.observer.price.problem.PriceAlert(105);
        var readers = new ChangeOnlyReaders(feed, chart, ticker, alert);
        int readsBefore = feed.reads();
        for (int tenth = 1; tenth <= 100; tenth++) {
            if (tenth == 20) feed.setPrice(102);
            if (tenth == 55) { feed.setPrice(106); feed.setPrice(100); }   // a spike between polls
            if (tenth == 80) feed.setPrice(103);
            readers.poll();
        }
        System.out.println("Polling:   chart " + chart.points() + ", alert fired: " + alert.fired()
                + ", reads " + (feed.reads() - readsBefore));

        // The Observer pattern: the feed tells its listeners.
        PriceFeed observed = new PriceFeed("ACME", 100);
        Chart observedChart = new Chart();
        Ticker observedTicker = new Ticker();
        PriceAlert observedAlert = new PriceAlert(105);
        observed.subscribe(observedChart);
        observed.subscribe(observedTicker);
        observed.subscribe(observedAlert);
        List<String> log = new ArrayList<>();
        observed.subscribe(change -> log.add(change.oldPrice() + "->" + change.newPrice()));

        observed.setPrice(102);
        observed.setPrice(106);
        observed.setPrice(100);
        observed.setPrice(103);
        System.out.println("Listeners: chart " + observedChart.points() + ", alert fired: "
                + observedAlert.fired() + ", ticker " + observedTicker.shown() + ", lambda " + log);
    }
}
