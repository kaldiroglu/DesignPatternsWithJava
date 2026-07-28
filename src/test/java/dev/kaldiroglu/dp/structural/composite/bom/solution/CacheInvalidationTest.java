package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GoF's "Caching to improve performance" (p. 169) and the obligation it creates:
 * a change anywhere below a node must invalidate that node's cached answer.
 */
class CacheInvalidationTest {

    private ProductCatalog.Bicycle catalog;

    @BeforeEach
    void buildTheBicycle() {
        catalog = ProductCatalog.cityBicycle();
    }

    @Test
    @DisplayName("A roll-up is computed once and then remembered")
    void rollUpsAreMemoized() {
        Assembly bicycle = catalog.bicycle();
        assertFalse(bicycle.isCostCached());

        Money first = bicycle.totalCost();
        assertTrue(bicycle.isCostCached());

        // The identical object comes back — no second walk of the tree.
        assertSame(first, bicycle.totalCost());
    }

    @Test
    @DisplayName("A change two levels down invalidates the root's cache")
    void aDeepChangeInvalidatesEveryAncestor() {
        Assembly bicycle = catalog.bicycle();
        Assembly wheel = catalog.wheel();
        Assembly hub = catalog.hub();

        bicycle.totalCost(); // warm every cache on the path
        wheel.totalCost();
        hub.totalCost();
        assertTrue(bicycle.isCostCached());

        // Add a dust cap to the hub — the deepest assembly in the structure.
        hub.add(new Part("CAP-DUST", "Dust Cap", Money.of(0.75), 4), 2);

        assertFalse(hub.isCostCached());
        assertFalse(wheel.isCostCached());
        assertFalse(bicycle.isCostCached()); // the invalidation travelled upwards
    }

    @Test
    @DisplayName("An engineering change on a shared sub-assembly is felt everywhere it is used")
    void changingAQuantityUpdatesEveryRollUpAbove() {
        Assembly bicycle = catalog.bicycle();
        Assembly wheel = catalog.wheel();

        assertEquals(Money.of(396.00), bicycle.totalCost());
        assertEquals(5950, bicycle.totalWeightGrams());

        // 32 -> 36 spokes per wheel: +4 spokes at $0.40 and 5 g each.
        wheel.changeQuantity(catalog.spoke(), 36);

        assertEquals(Money.of(86.60), wheel.totalCost());   // 85.00 + 1.60
        assertEquals(1555, wheel.totalWeightGrams());       // 1535 + 20
        // Both wheels changed, so the product gains 2 x 1.60 and 2 x 20 g.
        assertEquals(Money.of(399.20), bicycle.totalCost());
        assertEquals(5990, bicycle.totalWeightGrams());
        assertEquals(88, bicycle.partCount());              // 80 + 2 x 4
    }

    @Test
    @DisplayName("Removing a component invalidates the ancestors too")
    void removingAComponentInvalidatesAncestors() {
        Assembly bicycle = catalog.bicycle();
        Assembly wheel = catalog.wheel();

        bicycle.totalCost();
        wheel.remove(catalog.hub());

        assertFalse(bicycle.isCostCached());
        assertEquals(Money.of(71.30), wheel.totalCost());    // 85.00 - 13.70
        assertEquals(Money.of(368.60), bicycle.totalCost()); // 396.00 - 2 x 13.70
    }

    @Test
    @DisplayName("Attaching a shared sub-assembly to a new parent keeps both parents correct")
    void invalidationFollowsEveryParent() {
        Assembly bicycle = catalog.bicycle();
        Assembly wheel = catalog.wheel();

        Assembly spareWheelset = new Assembly("WHEELSET-SPARE", "Spare Wheelset");
        spareWheelset.add(wheel, 2);

        bicycle.totalCost();
        spareWheelset.totalCost();
        assertTrue(bicycle.isCostCached());
        assertTrue(spareWheelset.isCostCached());

        wheel.changeQuantity(catalog.spoke(), 36);

        // One change, two independent products to correct.
        assertFalse(bicycle.isCostCached());
        assertFalse(spareWheelset.isCostCached());
        assertEquals(Money.of(173.20), spareWheelset.totalCost());
    }
}
