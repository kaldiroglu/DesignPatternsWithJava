package dev.kaldiroglu.dp.structural.proxy.hw.remote;

/**
 * The Subject: how much stock is on hand.
 * <p>
 * Note what the signature does <em>not</em> say. It does not say the answer may take a
 * second, may fail, or may never come back. That silence is the subject of this exercise.
 */
public interface InventoryService {

    int stockOf(String sku);
}
