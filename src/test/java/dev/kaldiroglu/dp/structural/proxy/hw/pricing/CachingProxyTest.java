package dev.kaldiroglu.dp.structural.proxy.hw.pricing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** A caching proxy is worth having only if the supplier is called fewer times. So count. */
class CachingProxyTest {

    private final SupplierPriceService supplier = new SupplierPriceService();
    private final Clock.ManualClock clock = Clock.manual();
    private final CachingPriceProxy proxy = new CachingPriceProxy(supplier, clock, 60_000);

    @Test
    @DisplayName("five identical requests cost one supplier call")
    void repeatsAreFree() {
        for (int i = 0; i < 5; i++) {
            proxy.priceOf("SKU-200");
        }
        assertEquals(1, supplier.calls());
        assertEquals(4, proxy.hits());
        assertEquals(1, proxy.misses());
    }

    @Test
    @DisplayName("the cached answer is the supplier's answer")
    void theAnswerIsUnchanged() {
        assertEquals(Money.of("249.00"), proxy.priceOf("SKU-200"));
        assertEquals(Money.of("249.00"), proxy.priceOf("SKU-200"));
    }

    @Test
    @DisplayName("a different item is a different question")
    void keyedBySku() {
        proxy.priceOf("SKU-100");
        proxy.priceOf("SKU-200");
        proxy.priceOf("SKU-100");

        assertEquals(2, supplier.calls());
        assertEquals(1, proxy.hits());
    }

    @Test
    @DisplayName("the entry expires when the clock says so — no test sleeps")
    void expiry() {
        proxy.priceOf("SKU-200");
        clock.advance(59_999);
        proxy.priceOf("SKU-200");
        assertEquals(1, supplier.calls(), "still inside the TTL");

        clock.advance(2);
        proxy.priceOf("SKU-200");
        assertEquals(2, supplier.calls(), "and now it has lapsed");
    }

    @Test
    @DisplayName("invalidation is manual, and that is the hard half")
    void invalidate() {
        proxy.priceOf("SKU-200");
        proxy.invalidate("SKU-200");
        proxy.priceOf("SKU-200");

        assertEquals(2, supplier.calls());
    }

    @Test
    @DisplayName("a proxy may answer without forwarding — which no decorator does")
    void forwardsSometimes() {
        proxy.priceOf("SKU-300");
        int afterFirst = supplier.calls();
        proxy.priceOf("SKU-300");

        assertEquals(afterFirst, supplier.calls(), "the second call never reached it");
        assertTrue(PriceService.class.isAssignableFrom(CachingPriceProxy.class));
        assertTrue(PriceService.class.isAssignableFrom(SupplierPriceService.class));
    }
}
