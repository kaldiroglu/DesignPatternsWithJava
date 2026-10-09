package dev.kaldiroglu.dp.behavioral.observer.price;

import dev.kaldiroglu.dp.behavioral.observer.Printed;
import dev.kaldiroglu.dp.behavioral.observer.price.solution.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The price feed with listeners. Every figure the Part 3 slides quote about it is asserted
 * here.
 */
class SolutionTest {

    @Test
    @DisplayName("with listeners the chart shows 102, 106, 100, 103, and the alert at 105 fires")
    void listenersSeeEveryChange() {
        PriceFeed feed = new PriceFeed("ACME", 100);
        Chart chart = new Chart();
        Ticker ticker = new Ticker();
        PriceAlert alert = new PriceAlert(105);
        feed.subscribe(chart);
        feed.subscribe(ticker);
        feed.subscribe(alert);

        for (int price : List.of(102, 106, 100, 103)) {
            feed.setPrice(price);
        }

        assertEquals(List.of(102, 106, 100, 103), chart.points());
        assertEquals(4, chart.points().size(), "told about all four changes");
        assertTrue(alert.fired());
        assertEquals("ACME 103 up", ticker.shown());
    }

    @Test
    @DisplayName("a lambda can listen, and it logs every change from the old price to the new one")
    void aLambdaListens() {
        assertTrue(PriceListener.class.isAnnotationPresent(FunctionalInterface.class));
        assertEquals(1, PriceListener.class.getDeclaredMethods().length);

        PriceFeed feed = new PriceFeed("ACME", 100);
        List<String> log = new ArrayList<>();
        feed.subscribe(change -> log.add(change.oldPrice() + "->" + change.newPrice()));
        for (int price : List.of(102, 106, 100, 103)) {
            feed.setPrice(price);
        }

        assertEquals(List.of("100->102", "102->106", "106->100", "100->103"), log);
    }

    @Test
    @DisplayName("an unchanged price tells nobody")
    void noChangeNoNotification() {
        PriceFeed feed = new PriceFeed("ACME", 100);
        Chart chart = new Chart();
        feed.subscribe(chart);

        feed.setPrice(100);

        assertEquals(List.of(), chart.points());
    }

    @Test
    @DisplayName("a listener can stop listening while the program runs")
    void unsubscribe() {
        PriceFeed feed = new PriceFeed("ACME", 100);
        Chart chart = new Chart();
        feed.subscribe(chart);
        feed.setPrice(101);
        feed.unsubscribe(chart);
        feed.setPrice(102);

        assertEquals(List.of(101), chart.points());
    }

    @Test
    @DisplayName("a listener that unsubscribes while it is being told does not break the loop")
    void unsubscribeDuringNotification() {
        PriceFeed feed = new PriceFeed("ACME", 100);
        Chart chart = new Chart();
        PriceListener once = new PriceListener() {
            @Override
            public void priceChanged(PriceChange change) {
                feed.unsubscribe(this);
            }
        };
        feed.subscribe(once);
        feed.subscribe(chart);

        assertDoesNotThrow(() -> feed.setPrice(101));
        feed.setPrice(102);
        assertEquals(List.of(101, 102), chart.points());
    }

    @Test
    @DisplayName("the price is set before the listeners are told")
    void theSubjectIsConsistentWhenItNotifies() {
        PriceFeed feed = new PriceFeed("ACME", 100);
        List<Integer> seen = new ArrayList<>();
        feed.subscribe(change -> seen.add(feed.price()));

        feed.setPrice(104);

        assertEquals(List.of(104), seen);
    }

    @Test
    @DisplayName("the feed knows only PriceListener, and no listener reads the feed")
    void theFeedKnowsOnlyTheInterface() {
        List<Class<?>> feedFields = Arrays.stream(PriceFeed.class.getDeclaredFields())
                .<Class<?>>map(Field::getType).toList();
        assertFalse(feedFields.contains(Chart.class));
        assertFalse(feedFields.contains(Ticker.class));
        assertFalse(feedFields.contains(PriceAlert.class));

        for (Class<?> listener : List.of(Chart.class, Ticker.class, PriceAlert.class)) {
            assertTrue(PriceListener.class.isAssignableFrom(listener));
            assertTrue(Arrays.stream(listener.getDeclaredFields())
                    .noneMatch(f -> f.getType() == PriceFeed.class), listener.getSimpleName());
        }
    }

    @Test
    @DisplayName("Main prints 100 reads and no alert for polling, and every change and the alert for listeners")
    void mainOutput() {
        List<String> lines = Printed.by(() -> Main.main(new String[0]));

        assertEquals(List.of(
                "Polling:   chart [102, 100, 103], alert fired: false, reads 100",
                "Listeners: chart [102, 106, 100, 103], alert fired: true, ticker ACME 103 up, "
                        + "lambda [100->102, 102->106, 106->100, 100->103]"), lines);
    }
}
