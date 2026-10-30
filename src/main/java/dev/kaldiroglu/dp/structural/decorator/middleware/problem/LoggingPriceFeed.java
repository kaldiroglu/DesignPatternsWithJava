package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.ManualClock;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

/** Logging, as a subclass. */
public class LoggingPriceFeed extends BasicPriceFeed {

    private final CallLog log;

    public LoggingPriceFeed(ManualClock clock, CallLog log) {
        super(clock);
        this.log = log;
    }

    protected CallLog log() {
        return log;
    }

    @Override
    public Quote quoteFor(String sku) {
        log.record("asking for " + sku);
        Quote quote = super.quoteFor(sku);
        log.record("got " + quote);
        return quote;
    }
}
