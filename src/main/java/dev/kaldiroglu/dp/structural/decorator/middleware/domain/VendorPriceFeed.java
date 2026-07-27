package dev.kaldiroglu.dp.structural.decorator.middleware.domain;

/**
 * A price feed that arrived in a vendor's jar, and is {@code final}.
 * <p>
 * This class exists to settle an argument. Adding behavior by subclassing is not merely
 * awkward here — it is <em>impossible</em>, and the compiler says so. Decoration needs
 * nothing from the class it wraps except that it implement the interface, so every
 * decorator in the solution package works with this feed unchanged.
 * <p>
 * Vendors really do this, and so does the standard library: {@code java.lang.String},
 * {@code java.time.Instant} and most records are final for good reasons of their own.
 */
public final class VendorPriceFeed implements PriceFeed {

    private int callCount;

    @Override
    public Quote quoteFor(String sku) {
        callCount++;
        return Quote.of(sku, "42.00"); // the vendor's flat rate
    }

    public int callCount() {
        return callCount;
    }
}
