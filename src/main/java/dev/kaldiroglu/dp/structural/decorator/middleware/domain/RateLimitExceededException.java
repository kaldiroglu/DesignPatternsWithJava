package dev.kaldiroglu.dp.structural.decorator.middleware.domain;

/** Our own quota for this supplier is used up for now. */
public final class RateLimitExceededException extends PriceFeedException {

    public RateLimitExceededException(int limit) {
        super("rate limit of " + limit + " calls per window exceeded");
    }

    @Override
    public boolean isRetryable() {
        // Retrying immediately would only burn the quota further. Whether to wait and try
        // later is a decision for the caller, not for a retry loop with no sense of time.
        return false;
    }
}
