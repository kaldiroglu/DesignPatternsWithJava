package dev.kaldiroglu.dp.structural.flyweight.quote.problem;

import java.math.BigDecimal;

public class Demo {
    public static void main(String[] args) {
        FeedHandler feed = new FeedHandler();

        for (int i = 0; i < 250_000; i++) {
            feed.onTick("AAPL", "NASDAQ", "USD", "US0378331005", "Technology",
                "Apple Inc.", new BigDecimal("0.01"), 100,
                new BigDecimal("190.00"), new BigDecimal("190.05"), new BigDecimal("190.02"), 100);
            feed.onTick("MSFT", "NASDAQ", "USD", "US5949181045", "Technology",
                "Microsoft Corporation", new BigDecimal("0.01"), 100,
                new BigDecimal("420.10"), new BigDecimal("420.15"), new BigDecimal("420.12"), 50);
            feed.onTick("BMW.DE", "XETRA", "EUR", "DE0005190003", "Automotive",
                "Bayerische Motoren Werke AG", new BigDecimal("0.01"), 1,
                new BigDecimal("90.50"), new BigDecimal("90.55"), new BigDecimal("90.52"), 200);
            feed.onTick("7203.T", "TSE", "JPY", "JP3633400001", "Automotive",
                "Toyota Motor Corporation", new BigDecimal("0.5"), 100,
                new BigDecimal("3000"), new BigDecimal("3001"), new BigDecimal("3000"), 1000);
        }

        System.out.println("Quotes ingested: " + feed.total());
        // Each Quote retains its own copy of the metadata.
    }
}
