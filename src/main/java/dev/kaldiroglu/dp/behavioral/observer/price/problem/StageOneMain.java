package dev.kaldiroglu.dp.behavioral.observer.price.problem;

import java.util.List;

/**
 * Stage one: the feed calls every screen. Every price is seen, and the feed knows every
 * screen by class.
 */
public final class StageOneMain {

    public static void main(String[] args) {
        Chart chart = new Chart();
        PriceAlert alert = new PriceAlert(105);
        Ticker ticker = new Ticker();
        DirectPriceFeed direct = new DirectPriceFeed(chart, ticker, alert);
        for (int price : List.of(102, 106, 100, 103)) {
            direct.setPrice(price);
            System.out.println("Stage one, the feed calls every screen:\nchart " + chart.points() + "\nticker shows "
                    + ticker.shown() + "\nalert fired: " + alert.fired() + "\n");
        }
    }
}
