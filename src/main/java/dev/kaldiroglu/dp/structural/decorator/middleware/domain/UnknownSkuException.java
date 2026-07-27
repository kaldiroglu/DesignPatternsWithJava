package dev.kaldiroglu.dp.structural.decorator.middleware.domain;

/** The supplier does not sell this item. Asking again will not change that. */
public final class UnknownSkuException extends PriceFeedException {

    public UnknownSkuException(String sku) {
        super("unknown sku: " + sku);
    }

    @Override
    public boolean isRetryable() {
        return false;
    }
}
