package dev.kaldiroglu.dp.structural.flyweight.quote.solution;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;

/** Interns Instrument instances by (symbol, exchange). Thread-safe. */
public class InstrumentRegistry {

    private final ConcurrentHashMap<String, Instrument> cache = new ConcurrentHashMap<>();

    public Instrument get(String symbol, String exchange, String currency, String isin,
                          String sector, String companyName, BigDecimal tickSize, int lotSize,
                          LocalTime sessionOpen, LocalTime sessionClose) {
        String key = symbol + "@" + exchange;
        return cache.computeIfAbsent(key, k -> new Instrument(
            symbol, exchange, currency, isin, sector, companyName,
            tickSize, lotSize, sessionOpen, sessionClose));
    }

    public int size() { return cache.size(); }
}
