package dev.kaldiroglu.dp.structural.decorator.middleware.domain;

/** Anything that can go wrong when asking the supplier for a price. */
public abstract class PriceFeedException extends RuntimeException {

    protected PriceFeedException(String message) {
        super(message);
    }

    /**
     * Whether trying the same call again could plausibly succeed.
     * <p>
     * The retry decorator consults this instead of catching everything. Retrying a call
     * that can never succeed just multiplies the load on a supplier who is already saying
     * no — a real outage solution, and the reason a blanket {@code catch} is a bug.
     */
    public abstract boolean isRetryable();
}
