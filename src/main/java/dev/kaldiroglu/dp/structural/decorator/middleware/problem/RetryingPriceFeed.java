package dev.kaldiroglu.dp.structural.decorator.middleware.problem;

import dev.kaldiroglu.dp.structural.decorator.middleware.domain.FeedUnavailableException;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.PriceFeed;
import dev.kaldiroglu.dp.structural.decorator.middleware.domain.Quote;

/** Retrying, as a subclass. */
public class RetryingPriceFeed extends BasicPriceFeed {

    private final int maxAttempts;

    public RetryingPriceFeed(PriceFeed supplier, int maxAttempts) {
        super(supplier);
        this.maxAttempts = maxAttempts;
    }

    protected int maxAttempts() {
        return maxAttempts;
    }

    @Override
    public Quote quoteFor(String sku) {
        FeedUnavailableException last = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return super.quoteFor(sku);
            } catch (FeedUnavailableException e) {
                last = e;
            }
        }
        throw last;
    }
}
