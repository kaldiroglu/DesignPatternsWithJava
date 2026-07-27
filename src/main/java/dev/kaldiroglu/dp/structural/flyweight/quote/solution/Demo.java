package dev.kaldiroglu.dp.structural.flyweight.quote.solution;

import java.math.BigDecimal;
import java.time.LocalTime;

public class Demo {
    public static void main(String[] args) {
        InstrumentRegistry registry = new InstrumentRegistry();
        FeedHandler feed = new FeedHandler(registry);

        Instrument aapl = registry.get("AAPL", "NASDAQ", "USD", "US0378331005", "Technology",
            "Apple Inc.", new BigDecimal("0.01"), 100,
            LocalTime.of(9, 30), LocalTime.of(16, 0));
        Instrument msft = registry.get("MSFT", "NASDAQ", "USD", "US5949181045", "Technology",
            "Microsoft Corporation", new BigDecimal("0.01"), 100,
            LocalTime.of(9, 30), LocalTime.of(16, 0));
        Instrument bmw  = registry.get("BMW.DE", "XETRA", "EUR", "DE0005190003", "Automotive",
            "Bayerische Motoren Werke AG", new BigDecimal("0.01"), 1,
            LocalTime.of(9, 0), LocalTime.of(17, 30));
        Instrument toyota = registry.get("7203.T", "TSE", "JPY", "JP3633400001", "Automotive",
            "Toyota Motor Corporation", new BigDecimal("0.5"), 100,
            LocalTime.of(9, 0), LocalTime.of(15, 0));

        for (int i = 0; i < 250_000; i++) {
            feed.onTick(aapl, new BigDecimal("190.00"), new BigDecimal("190.05"), new BigDecimal("190.02"), 100);
            feed.onTick(msft, new BigDecimal("420.10"), new BigDecimal("420.15"), new BigDecimal("420.12"), 50);
            feed.onTick(bmw,  new BigDecimal("90.50"),  new BigDecimal("90.55"),  new BigDecimal("90.52"),  200);
            feed.onTick(toyota, new BigDecimal("3000"), new BigDecimal("3001"),   new BigDecimal("3000"),  1000);
        }

        System.out.println("Quotes ingested:        " + feed.total());
        System.out.println("Distinct Instruments:   " + feed.distinctInstruments());
        System.out.println("Identity reuse (AAPL):  " + (registry.get("AAPL", "NASDAQ", "USD",
            "US0378331005", "Technology", "Apple Inc.", new BigDecimal("0.01"), 100,
            LocalTime.of(9, 30), LocalTime.of(16, 0)) == aapl));
    }
}
