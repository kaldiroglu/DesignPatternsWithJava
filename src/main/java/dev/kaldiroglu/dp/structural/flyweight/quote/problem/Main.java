package dev.kaldiroglu.dp.structural.flyweight.quote.problem;

import java.math.BigDecimal;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Ingests a morning of ticks for four instruments and counts what is left in memory.
 *
 * <p>Four instruments. The count of distinct symbol objects retained is not four.</p>
 */
public class Main {

    static final int TICKS_PER_INSTRUMENT = 25_000;

    public static void main(String[] args) {
        Wire.reset();
        FeedHandler feed = new FeedHandler();

        for (int i = 0; i < TICKS_PER_INSTRUMENT; i++) {
            tick(feed, "AAPL", "NASDAQ", "USD", "US0378331005", "Technology", "Apple Inc.",
                    "0.01", 100, "190.00", "190.05", "190.02", 100);
            tick(feed, "MSFT", "NASDAQ", "USD", "US5949181045", "Technology",
                    "Microsoft Corporation", "0.01", 100, "420.10", "420.15", "420.12", 50);
            tick(feed, "BMW.DE", "XETRA", "EUR", "DE0005190003", "Automotive",
                    "Bayerische Motoren Werke AG", "0.01", 1, "90.50", "90.55", "90.52", 200);
            tick(feed, "7203.T", "TSE", "JPY", "JP3633400001", "Automotive",
                    "Toyota Motor Corporation", "0.5", 100, "3000", "3001", "3000", 1000);
        }

        Map<String, Boolean> distinctSymbolObjects = new IdentityHashMap<>();
        for (Quote q : feed.quotes()) {
            distinctSymbolObjects.put(q.symbol(), Boolean.TRUE);
        }

        System.out.printf("Quotes ingested                     : %,d%n", feed.total());
        System.out.println("Instruments in the universe         : 4");
        System.out.printf("Distinct symbol objects retained    : %,d%n",
                distinctSymbolObjects.size());
        System.out.printf("Strings decoded off the wire        : %,d%n", Wire.decodeCount());
        System.out.println();
        System.out.println("Four instruments. One symbol object per tick, held forever.");
    }

    /** One tick, with every field arriving off the wire as a fresh object. */
    private static void tick(FeedHandler feed, String symbol, String exchange, String currency,
                             String isin, String sector, String companyName, String tickSize,
                             int lotSize, String bid, String ask, String last, long volume) {
        feed.onTick(Wire.decode(symbol), Wire.decode(exchange), Wire.decode(currency),
                Wire.decode(isin), Wire.decode(sector), Wire.decode(companyName),
                new BigDecimal(tickSize), lotSize,
                new BigDecimal(bid), new BigDecimal(ask), new BigDecimal(last), volume);
    }
}
