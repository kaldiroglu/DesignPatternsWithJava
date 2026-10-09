package dev.kaldiroglu.dp.behavioral.observer.price;

import dev.kaldiroglu.dp.behavioral.observer.price.problem.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The three designs of Part 1: the feed calls every screen, the screens poll, and the
 * screens poll often and act only on a change. Every figure the Part 1 slides quote is
 * asserted here.
 */
class ProblemTest {

    /** Ten seconds, ten polls a second. The price spikes to 106 and back between two polls. */
    private static void tenSeconds(PriceFeed feed, ChangeOnlyReaders readers) {
        for (int tenth = 1; tenth <= 100; tenth++) {
            if (tenth == 20) feed.setPrice(102);
            if (tenth == 55) { feed.setPrice(106); feed.setPrice(100); }
            if (tenth == 80) feed.setPrice(103);
            readers.poll();
        }
    }

    @Test
    @DisplayName("stage one sees every price, but the feed knows every screen by class")
    void theFeedCallsEveryScreen() {
        Chart chart = new Chart();
        Ticker ticker = new Ticker();
        PriceAlert alert = new PriceAlert(105);
        DirectPriceFeed feed = new DirectPriceFeed(chart, ticker, alert);

        for (int price : List.of(102, 106, 100, 103)) {
            feed.setPrice(price);
        }

        assertEquals(List.of(102, 106, 100, 103), chart.points());
        assertEquals(103, ticker.shown());
        assertTrue(alert.fired());

        List<Class<?>> fieldTypes = Arrays.stream(DirectPriceFeed.class.getDeclaredFields())
                .<Class<?>>map(Field::getType).toList();
        assertTrue(fieldTypes.containsAll(List.of(Chart.class, Ticker.class, PriceAlert.class)));
    }

    @Test
    @DisplayName("stage two: the feed knows nobody, and most polls find the same price")
    void theScreensPoll() {
        PriceFeed feed = new PriceFeed(100);
        Chart chart = new Chart();
        PollingReaders readers = new PollingReaders(feed, chart, new Ticker(), new PriceAlert(105));

        readers.poll();
        readers.poll();
        feed.setPrice(102);
        readers.poll();

        assertEquals(List.of(100, 100, 102), chart.points(), "the chart draws the same price again");
        assertTrue(Arrays.stream(PriceFeed.class.getDeclaredFields())
                .allMatch(f -> f.getType() == int.class), "the feed holds only numbers");
    }

    @Test
    @DisplayName("stage three: 100 reads, four changes, three of them seen, and the alert at 105 never fires")
    void stageThreeMissesTheSpike() {
        PriceFeed feed = new PriceFeed(100);
        Chart chart = new Chart();
        Ticker ticker = new Ticker();
        PriceAlert alert = new PriceAlert(105);
        ChangeOnlyReaders readers = new ChangeOnlyReaders(feed, chart, ticker, alert);
        int readsBefore = feed.reads();

        tenSeconds(feed, readers);

        assertEquals(100, feed.reads() - readsBefore);
        assertEquals(List.of(102, 100, 103), chart.points());
        assertEquals(3, chart.points().size(), "three of the four changes are seen");
        assertFalse(chart.points().contains(106));
        assertFalse(alert.fired());
        assertEquals(103, ticker.shown());
    }

    @Test
    @DisplayName("stage three: an unchanged price costs one read and no work")
    void anUnchangedPriceCostsOneRead() {
        PriceFeed feed = new PriceFeed(100);
        Chart chart = new Chart();
        ChangeOnlyReaders readers = new ChangeOnlyReaders(feed, chart, new Ticker(), new PriceAlert(105));
        int readsBefore = feed.reads();

        readers.poll();

        assertEquals(1, feed.reads() - readsBefore);
        assertEquals(List.of(), chart.points());
    }
}
