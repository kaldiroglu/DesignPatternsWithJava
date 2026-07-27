package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

/** Logging, as a subclass. */
public class LoggingPriceFeed extends BasicPriceFeed {

    private final CallLog log;

    public LoggingPriceFeed(PriceFeed supplier, CallLog log) {
        super(supplier);
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
