package dev.kaldiroglu.dp.structural.proxy.hw.pricing;

/**
 * Homework 1 — the caching proxy.
 */
public class Main {

    public static void main(String[] args) {
        SupplierPriceService supplier = new SupplierPriceService();
        Clock.ManualClock clock = Clock.manual();
        CachingPriceProxy proxy = new CachingPriceProxy(supplier, clock, 60_000);

        for (int i = 0; i < 5; i++) {
            proxy.priceOf("SKU-200");
        }
        System.out.printf("five requests -> %d supplier call(s), %d hit(s)%n",
                supplier.calls(), proxy.hits());

        clock.advance(61_000);
        proxy.priceOf("SKU-200");
        System.out.printf("after the TTL expires -> %d supplier call(s)%n", supplier.calls());

        System.out.println("""

                The saving is real and it is the easy half.

                The hard half is that between the supplier changing a price and
                the TTL expiring, this proxy returns a number that is wrong — and
                confidently. No pattern fixes that; somebody has to decide how
                stale the business can afford to be.""");
    }
}
