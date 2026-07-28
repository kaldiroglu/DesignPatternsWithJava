package dev.kaldiroglu.dp.structural.proxy.hw.pricing;

import java.util.Map;

/**
 * The RealSubject: the supplier's service, which charges per call.
 * <p>
 * It counts its calls, because the entire argument for putting a proxy in front of it is that
 * the number goes down.
 */
public class SupplierPriceService implements PriceService {

    private static final Map<String, String> CATALOG = Map.of(
            "SKU-100", "19.90", "SKU-200", "249.00", "SKU-300", "7.45");

    private int calls;

    @Override
    public Money priceOf(String sku) {
        calls++;
        String amount = CATALOG.get(sku);
        if (amount == null) {
            throw new IllegalArgumentException("no such item: " + sku);
        }
        return Money.of(amount);
    }

    public int calls() {
        return calls;
    }
}
