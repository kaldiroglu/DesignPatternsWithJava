package dev.kaldiroglu.dp.behavioral.observer.price;

import dev.kaldiroglu.dp.behavioral.observer.Printed;
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

    private interface MainMethod {
        void run(String[] args) throws InterruptedException;
    }

    private static void interruptible(MainMethod main) {
        try {
            main.run(new String[0]);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
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
    @DisplayName("stages two and three: the reader classes hold every screen, so a new screen is an edit to them")
    void aNewScreenIsAnEditToTheReaders() {
        for (Class<?> readers : List.of(PollingReaders.class, ChangeOnlyReaders.class)) {
            List<Class<?>> fieldTypes = Arrays.stream(readers.getDeclaredFields())
                    .<Class<?>>map(Field::getType).toList();
            assertTrue(fieldTypes.containsAll(List.of(Chart.class, Ticker.class, PriceAlert.class)));
        }
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

    @Test
    @DisplayName("StageOneMain prints the screens after each price, and the alert fires on 106")
    void stageOneMainOutput() {
        List<String> lines = Printed.by(() -> StageOneMain.main(new String[0]));

        assertEquals(20, lines.size(), "four prices, five lines each");
        assertEquals("alert fired: false", lines.get(3), "102 is under the limit");
        assertEquals("alert fired: true", lines.get(8), "106 is over the limit");
        assertEquals(List.of("Stage one, the feed calls every screen:", "chart [102, 106, 100, 103]",
                "ticker shows 103", "alert fired: true", ""), lines.subList(15, 20));
    }

    @Test
    @DisplayName("StageTwoMain: the thread polls three times, and each screen shows what it got")
    void stageTwoMainOutput() {
        String header = "Stage two, the screens poll every second:";
        assertEquals(List.of(
                header, "chart [100]", "ticker shows 100", "alert fired: false", "",
                header, "chart [100, 100]", "ticker shows 100", "alert fired: false", "",
                header, "chart [100, 100, 102]", "ticker shows 102", "alert fired: false", ""),
                Printed.by(() -> interruptible(StageTwoMain::main)));
    }

    @Test
    @DisplayName("StageThreeMain: the thread polls ten times a second, and only the changes reach the chart")
    void stageThreeMainOutput() {
        String header = "Stage three, the screens poll ten times a second:";
        assertEquals(List.of(
                header, "chart [102]", "ticker shows 102", "alert fired: false", "",
                header, "chart [102, 103]", "ticker shows 103", "alert fired: false", ""),
                Printed.by(() -> interruptible(StageThreeMain::main)));
    }

    @Test
    @DisplayName("the readers of stages two and three are threads")
    void theReadersAreThreads() {
        assertTrue(Thread.class.isAssignableFrom(PollingReaders.class));
        assertTrue(Thread.class.isAssignableFrom(ChangeOnlyReaders.class));
    }

    @Test
    @DisplayName("FastSpikeMain prints each screen after each change seen, no alert, and 100 reads")
    void fastSpikeMainOutput() {
        assertEquals(List.of(
                "Stage three, ten polls a second, poll 20:", "chart [102]", "ticker shows 102",
                "alert fired: false", "",
                "Stage three, ten polls a second, poll 55:", "chart [102, 100]", "ticker shows 100",
                "alert fired: false", "",
                "Stage three, ten polls a second, poll 80:", "chart [102, 100, 103]", "ticker shows 103",
                "alert fired: false", "",
                "Reads in ten seconds: 100"), Printed.by(() -> FastSpikeMain.main(new String[0])));
    }
}
