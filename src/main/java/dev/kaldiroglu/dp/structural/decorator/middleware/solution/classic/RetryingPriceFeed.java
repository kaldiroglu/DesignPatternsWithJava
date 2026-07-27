package dev.kaldiroglu.dp.structural.decorator.middleware.solution.classic;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeedException;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

/**
 * Calls the next feed again when the failure was worth retrying.
 * <p>
 * This is the decorator that calls {@code inner()} more than once. That is worth pointing
 * out: a decorator is not obliged to forward exactly one request. It may forward none (the
 * cache, on a hit), one (logging, timing), or several (this class). All it must do is
 * honor the interface.
 */
public final class RetryingPriceFeed extends PriceFeedDecorator {

    private final int maxAttempts;

    public RetryingPriceFeed(PriceFeed inner, int maxAttempts) {
        super(inner);
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be at least 1");
        }
        this.maxAttempts = maxAttempts;
    }

    @Override
    public Quote quoteFor(String sku) {
        PriceFeedException last = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return inner().quoteFor(sku);
            } catch (PriceFeedException e) {
                if (!e.isRetryable()) {
                    throw e; // an unknown SKU will still be unknown on the third try
                }
                last = e;
            }
        }
        throw last;
    }
}
