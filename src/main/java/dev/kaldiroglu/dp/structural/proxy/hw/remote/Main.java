package dev.kaldiroglu.dp.structural.proxy.hw.remote;

import java.util.List;

/**
 * Homework 2 — the remote proxy, and the loop that used to be free.
 */
public class Main {

    public static void main(String[] args) {
        WarehouseInventory warehouse = new WarehouseInventory();
        Link link = new Link(120);
        InventoryService inventory = new RemoteInventoryProxy(warehouse, link, 3);

        System.out.println("stock of SKU-200: " + inventory.stockOf("SKU-200"));
        System.out.printf("  one call: %d round trip, %d ms%n%n",
                link.roundTrips(), link.elapsedMillis());

        // The code below is ordinary, sensible, and would be free against a local object.
        List<String> basket = List.of("SKU-100", "SKU-200", "SKU-300",
                                      "SKU-100", "SKU-200", "SKU-300");
        int total = 0;
        for (String sku : basket) {
            total += inventory.stockOf(sku);
        }
        System.out.printf("a six-line basket: %d round trips, %d ms total%n",
                link.roundTrips(), link.elapsedMillis());
        System.out.println("""

                Nothing in InventoryService warned that a loop costs a round trip
                per iteration. The proxy did its job perfectly — it made a remote
                object look local — and that is precisely how the mistake is made.

                The exercise: what would you add to the interface, or to the proxy,
                so the caller finds out before production does?""");

        System.out.println("\n-- and when the link is down --");
        Link flaky = new Link(120).failNext(2);
        InventoryService retrying = new RemoteInventoryProxy(warehouse, flaky, 3);
        System.out.println("stock after two failures: " + retrying.stockOf("SKU-100"));
        System.out.printf("  %d round trips to answer one question%n", flaky.roundTrips());
    }
}
