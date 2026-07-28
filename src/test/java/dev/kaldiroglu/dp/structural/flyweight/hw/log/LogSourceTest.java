package dev.kaldiroglu.dp.structural.flyweight.hw.log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.IdentityHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Homework 2 — the logger.
 * <p>
 * Every log line repeats where it came from. A process emits millions of lines from a few
 * hundred call sites, so the metadata is the part worth interning.
 */
class LogSourceTest {

    private static final Clock FIXED =
            Clock.fixed(Instant.parse("2026-07-28T09:15:00Z"), ZoneOffset.UTC);

    @Test
    @DisplayName("thirty thousand lines from three call sites hold three source objects")
    void metadataIsInterned() {
        Logger logger = new Logger(new LogSourceRegistry(), FIXED);

        for (int i = 0; i < 10_000; i++) {
            logger.log("INFO", "orders", "OrderService", "confirm", "OrderService.java",
                    "order " + i + " confirmed");
            logger.log("WARN", "orders", "OrderService", "retry", "OrderService.java",
                    "retrying order " + i);
            logger.log("INFO", "billing", "InvoiceService", "issue", "InvoiceService.java",
                    "invoice for order " + i);
        }

        Map<LogSource, Boolean> distinct = new IdentityHashMap<>();
        for (LogEvent event : logger.events()) {
            distinct.put(event.source(), Boolean.TRUE);
        }

        assertEquals(30_000, logger.eventCount());
        assertEquals(3, distinct.size());
        assertEquals(3, logger.distinctSources());
    }

    @Test
    @DisplayName("the method name is part of the key, so two sites in one class stay apart")
    void theKeyIsTheWholeCallSite() {
        LogSourceRegistry registry = new LogSourceRegistry();

        LogSource confirm = registry.get("orders", "OrderService", "confirm", "OrderService.java");
        LogSource retry = registry.get("orders", "OrderService", "retry", "OrderService.java");
        LogSource confirmAgain =
                registry.get("orders", "OrderService", "confirm", "OrderService.java");

        assertNotSame(confirm, retry, "a key on the class alone would make the log lie");
        assertSame(confirm, confirmAgain);
        assertEquals(2, registry.size());
    }

    @Test
    @DisplayName("the event holds the message and the time, and nothing about the call site")
    void theEventCarriesOnlyWhatVaries() {
        Logger logger = new Logger(new LogSourceRegistry(), FIXED);
        logger.log("INFO", "orders", "OrderService", "confirm", "OrderService.java", "hello");

        LogEvent event = logger.events().get(0);

        assertEquals(4, LogEvent.class.getRecordComponents().length,
                "source, at, level, message");
        assertEquals("2026-07-28T09:15:00Z INFO OrderService.confirm(OrderService.java) - hello",
                event.render());
    }

    @Test
    @DisplayName("rendering reads the shared source rather than a copy on the event")
    void renderingReadsTheFlyweight() {
        LogSourceRegistry registry = new LogSourceRegistry();
        Logger logger = new Logger(registry, FIXED);

        logger.log("INFO", "orders", "OrderService", "confirm", "OrderService.java", "first");
        logger.log("INFO", "orders", "OrderService", "confirm", "OrderService.java", "second");

        assertSame(logger.events().get(0).source(), logger.events().get(1).source());
        assertEquals("OrderService.confirm(OrderService.java)",
                logger.events().get(1).source().toString());
    }
}
