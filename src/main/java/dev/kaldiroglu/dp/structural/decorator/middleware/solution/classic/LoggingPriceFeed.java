package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeedException;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

/**
 * Writes a line before the call and a line after it.
 * <p>
 * Where this decorator sits in the chain decides what it can see. Outside the retry
 * decorator it logs one attempt per request; inside it, one line per attempt. Neither is
 * more correct — but the choice is now made by the person assembling the chain, at the
 * moment they assemble it.
 */
public final class LoggingPriceFeed extends PriceFeedDecorator {

    private final CallLog log;
    private final String name;

    public LoggingPriceFeed(PriceFeed inner, CallLog log) {
        this(inner, log, "feed");
    }

    /** The name appears in every line, so a chain with two loggers stays readable. */
    public LoggingPriceFeed(PriceFeed inner, CallLog log, String name) {
        super(inner);
        this.log = log;
        this.name = name;
    }

    @Override
    public Quote quoteFor(String sku) {
        log.record(name + ": asking for " + sku);
        try {
            Quote quote = inner().quoteFor(sku);
            log.record(name + ": got " + quote);
            return quote;
        } catch (PriceFeedException e) {
            log.record(name + ": failed for " + sku + " — " + e.getMessage());
            throw e; // a decorator observes; it does not swallow
        }
    }
}
