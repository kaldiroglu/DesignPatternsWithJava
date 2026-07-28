package dev.kaldiroglu.dp.structural.flyweight.quote.solution;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>FlyweightFactory</b>. One {@link Instrument} per traded symbol, however many ticks arrive.
 *
 * <p>Thread-safe on purpose: a feed handler is usually several threads deep, and two of them
 * decoding the first AAPL tick at the same moment must still end up with one instrument.
 * {@code computeIfAbsent} gives that guarantee; a get-then-put would not.</p>
 *
 * <p>The registry is also where a subtle bug used to live. The key is
 * {@code symbol@exchange}, but the method took the full metadata — so a second call with a
 * corrected sector or a changed tick size silently returned the first instrument and threw
 * the new values away. Reference data does get corrected during a session, and losing the
 * correction without a word is worse than either accepting or rejecting it.</p>
 *
 * <p>{@link #define} now states which it does: definitions are made once, and a conflicting
 * redefinition is an error rather than a shrug. {@link #get} is the hot path and takes only
 * the key.</p>
 */
public class InstrumentRegistry {

    private final Map<String, Instrument> cache = new ConcurrentHashMap<>();

    /** Defines an instrument, or returns the identical one already defined. */
    public Instrument define(String symbol, String exchange, String currency, String isin,
                             String sector, String companyName, BigDecimal tickSize, int lotSize,
                             LocalTime sessionOpen, LocalTime sessionClose) {
        Instrument candidate = new Instrument(symbol, exchange, currency, isin, sector,
                companyName, tickSize, lotSize, sessionOpen, sessionClose);

        Instrument existing = cache.putIfAbsent(key(symbol, exchange), candidate);
        if (existing == null) {
            return candidate;
        }
        if (!existing.hasSameDefinitionAs(candidate)) {
            throw new IllegalStateException(
                    "instrument " + key(symbol, exchange) + " is already defined differently; "
                            + "a flyweight's intrinsic state cannot change under its holders");
        }
        return existing;
    }

    /** The shared instrument for a symbol, or null if the feed sent one nobody defined. */
    public Instrument get(String symbol, String exchange) {
        return cache.get(key(symbol, exchange));
    }

    public int size() {
        return cache.size();
    }

    private static String key(String symbol, String exchange) {
        return symbol + "@" + exchange;
    }
}
