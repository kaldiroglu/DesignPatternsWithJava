package dev.kaldiroglu.dp.structural.proxy.hw.remote;

import java.util.HashMap;
import java.util.Map;

/** The RealSubject: the warehouse system, as it would run inside the same process. */
public class WarehouseInventory implements InventoryService {

    private final Map<String, Integer> stock = new HashMap<>(
            Map.of("SKU-100", 42, "SKU-200", 7, "SKU-300", 0));

    @Override
    public int stockOf(String sku) {
        Integer onHand = stock.get(sku);
        if (onHand == null) {
            throw new IllegalArgumentException("no such item: " + sku);
        }
        return onHand;
    }

    public void set(String sku, int quantity) {
        stock.put(sku, quantity);
    }
}
