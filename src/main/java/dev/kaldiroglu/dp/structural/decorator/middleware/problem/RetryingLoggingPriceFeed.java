package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.CallLog;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

/**
 * Retrying <em>and</em> logging. The third class, and the first sign of trouble.
 * <p>
 * It extends {@link RetryingPriceFeed}, so it inherits the retry loop — and then has to
 * repeat the two log lines from {@link LoggingPriceFeed} verbatim, because a class cannot
 * inherit from both.
 * <p>
 * Notice also what this class quietly decides for everyone: the logging happens
 * <em>outside</em> the retry loop, so three attempts produce one log line, not three.
 * That may be what you want. If it is not, the alternative is another class.
 */
public class RetryingLoggingPriceFeed extends RetryingPriceFeed {

    private final CallLog log;

    public RetryingLoggingPriceFeed(PriceFeed supplier, int maxAttempts, CallLog log) {
        super(supplier, maxAttempts);
        this.log = log;
    }

    protected CallLog log() {
        return log;
    }

    @Override
    public Quote quoteFor(String sku) {
        // Copied from LoggingPriceFeed.
        log.record("asking for " + sku);
        Quote quote = super.quoteFor(sku);
        log.record("got " + quote);
        return quote;
    }
}
