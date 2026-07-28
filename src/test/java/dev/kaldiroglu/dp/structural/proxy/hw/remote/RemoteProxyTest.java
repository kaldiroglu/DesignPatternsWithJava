package dev.kaldiroglu.dp.structural.proxy.hw.remote;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** The proxy makes a remote object look local. These tests measure what that hides. */
class RemoteProxyTest {

    private final WarehouseInventory warehouse = new WarehouseInventory();

    @Test
    @DisplayName("the answer is the warehouse's answer")
    void substitutable() {
        Link link = new Link(120);
        InventoryService inventory = new RemoteInventoryProxy(warehouse, link, 3);

        assertEquals(warehouse.stockOf("SKU-200"), inventory.stockOf("SKU-200"));
        assertTrue(InventoryService.class.isAssignableFrom(RemoteInventoryProxy.class));
    }

    @Test
    @DisplayName("one question, one round trip — and the interface never said so")
    void everyCallCrossesTheNetwork() {
        Link link = new Link(120);
        InventoryService inventory = new RemoteInventoryProxy(warehouse, link, 3);

        inventory.stockOf("SKU-100");

        assertEquals(1, link.roundTrips());
        assertEquals(120, link.elapsedMillis());
    }

    @Test
    @DisplayName("a loop that was free locally costs a round trip per iteration")
    void theLoopThatUsedToBeFree() {
        Link link = new Link(120);
        InventoryService inventory = new RemoteInventoryProxy(warehouse, link, 3);
        List<String> basket = List.of("SKU-100", "SKU-200", "SKU-300",
                                      "SKU-100", "SKU-200", "SKU-300");

        basket.forEach(inventory::stockOf);

        assertEquals(6, link.roundTrips());
        assertEquals(720, link.elapsedMillis(), "six-tenths of a second for six numbers");
    }

    @Test
    @DisplayName("brief outages are hidden by the retry, at the cost of extra trips")
    void retryHidesFailure() {
        Link link = new Link(120).failNext(2);
        InventoryService inventory = new RemoteInventoryProxy(warehouse, link, 3);

        assertEquals(42, inventory.stockOf("SKU-100"));
        assertEquals(3, link.roundTrips(), "three trips to answer one question");
    }

    @Test
    @DisplayName("a real outage costs the full retry budget, then fails")
    void retryDoublesTheDelayOfARealOutage() {
        Link link = new Link(120).failNext(5);
        InventoryService inventory = new RemoteInventoryProxy(warehouse, link, 3);

        RemoteCallFailedException thrown = assertThrows(RemoteCallFailedException.class,
                () -> inventory.stockOf("SKU-100"));

        assertTrue(thrown.getMessage().contains("gave up after 3 attempts"));
        assertEquals(3, link.roundTrips());
        assertEquals(360, link.elapsedMillis());
    }

    @Test
    @DisplayName("the interface admits none of this")
    void theInterfaceIsSilent() throws Exception {
        var method = InventoryService.class.getDeclaredMethod("stockOf", String.class);

        assertEquals(0, method.getExceptionTypes().length,
                "no checked exception warns the caller that this crosses a network");
    }
}
