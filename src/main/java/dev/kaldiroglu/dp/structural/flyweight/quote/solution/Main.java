package dev.kaldiroglu.dp.structural.flyweight.quote.solution;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * The same morning of ticks, with the instrument metadata interned.
 *
 * <p>Same four instruments, same hundred thousand events. The count that changes is the one
 * the problem version could not keep down: distinct symbol objects retained.</p>
 */
public class Main {

    static final int TICKS_PER_INSTRUMENT = 25_000;

    public static void main(String[] args) {
        InstrumentRegistry registry = new InstrumentRegistry();
        FeedHandler feed = new FeedHandler(registry);

        // Reference data is defined once, before the session opens.
        Instrument aapl = registry.define("AAPL", "NASDAQ", "USD", "US0378331005", "Technology",
                "Apple Inc.", new BigDecimal("0.01"), 100, LocalTime.of(9, 30), LocalTime.of(16, 0));
        Instrument msft = registry.define("MSFT", "NASDAQ", "USD", "US5949181045", "Technology",
                "Microsoft Corporation", new BigDecimal("0.01"), 100,
                LocalTime.of(9, 30), LocalTime.of(16, 0));
        Instrument bmw = registry.define("BMW.DE", "XETRA", "EUR", "DE0005190003", "Automotive",
                "Bayerische Motoren Werke AG", new BigDecimal("0.01"), 1,
                LocalTime.of(9, 0), LocalTime.of(17, 30));
        Instrument toyota = registry.define("7203.T", "TSE", "JPY", "JP3633400001", "Automotive",
                "Toyota Motor Corporation", new BigDecimal("0.5"), 100,
                LocalTime.of(9, 0), LocalTime.of(15, 0));

        for (int i = 0; i < TICKS_PER_INSTRUMENT; i++) {
            feed.onTick(aapl, new BigDecimal("190.00"), new BigDecimal("190.05"),
                    new BigDecimal("190.02"), 100);
            feed.onTick(msft, new BigDecimal("420.10"), new BigDecimal("420.15"),
                    new BigDecimal("420.12"), 50);
            feed.onTick(bmw, new BigDecimal("90.50"), new BigDecimal("90.55"),
                    new BigDecimal("90.52"), 200);
            feed.onTick(toyota, new BigDecimal("3000"), new BigDecimal("3001"),
                    new BigDecimal("3000"), 1000);
        }

        Map<String, Boolean> distinctSymbolObjects = new IdentityHashMap<>();
        for (Quote q : feed.quotes()) {
            distinctSymbolObjects.put(q.instrument().symbol(), Boolean.TRUE);
        }

        System.out.printf("Quotes ingested                     : %,d%n", feed.total());
        System.out.println("Instruments in the universe         : " + feed.distinctInstruments());
        System.out.printf("Distinct symbol objects retained    : %,d%n",
                distinctSymbolObjects.size());
        System.out.println();
        System.out.println("Identity reuse: the feed's AAPL is the registry's AAPL : "
                + (registry.get("AAPL", "NASDAQ") == aapl));
    }
}
