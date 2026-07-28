package dev.kaldiroglu.dp.structural.flyweight.hw.log;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.IdentityHashMap;
import java.util.Map;

/** A hundred thousand log lines from three call sites. */
public class Main {

    private static final int LINES = 100_000;

    public static void main(String[] args) {
        Clock clock = Clock.fixed(Instant.parse("2026-07-28T09:15:00Z"), ZoneOffset.UTC);
        Logger logger = new Logger(new LogSourceRegistry(), clock);

        for (int i = 0; i < LINES; i++) {
            logger.log("INFO", "orders", "OrderService", "confirm", "OrderService.java",
                    "order " + i + " confirmed");
            logger.log("WARN", "orders", "OrderService", "retry", "OrderService.java",
                    "retrying order " + i);
            logger.log("INFO", "billing", "InvoiceService", "issue", "InvoiceService.java",
                    "invoice for order " + i);
        }

        Map<LogSource, Boolean> distinctObjects = new IdentityHashMap<>();
        for (LogEvent event : logger.events()) {
            distinctObjects.put(event.source(), Boolean.TRUE);
        }

        System.out.printf("Log lines emitted        : %,d%n", logger.eventCount());
        System.out.println("Call sites in the code   : 3");
        System.out.println("LogSource objects held   : " + distinctObjects.size());
        System.out.println();
        System.out.println("First line: " + logger.events().get(0).render());
    }
}
