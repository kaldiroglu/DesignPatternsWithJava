package dev.kaldiroglu.dp.structural.decorator.middleware.domain;

/** The supplier could not be reached. Trying again may well work. */
public final class FeedUnavailableException extends PriceFeedException {

    public FeedUnavailableException(String message) {
        super(message);
    }

    @Override
    public boolean isRetryable() {
        return true;
    }
}
