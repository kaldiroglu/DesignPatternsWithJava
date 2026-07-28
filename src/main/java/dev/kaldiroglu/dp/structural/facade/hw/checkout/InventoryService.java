package dev.kaldiroglu.dp.structural.facade.hw.checkout;

import java.util.HashMap;
import java.util.Map;

/** A subsystem class. Useful, and no client should have to know it exists. */
public class InventoryService {

    private final Map<String, Integer> stock = new HashMap<>(Map.of("SKU-100", 5, "SKU-200", 0));
    private int reservations;

    public boolean reserve(String sku, int quantity) {
        int onHand = stock.getOrDefault(sku, 0);
        if (onHand < quantity) {
            return false;
        }
        stock.put(sku, onHand - quantity);
        reservations++;
        return true;
    }

    public void release(String sku, int quantity) {
        stock.merge(sku, quantity, Integer::sum);
        reservations--;
    }

    public int reservations() {
        return reservations;
    }
}
