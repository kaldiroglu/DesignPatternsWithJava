package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The roll-up arithmetic: cost, weight and part count, at every level of the
 * sample bicycle.
 */
class RollUpTest {

    private ProductCatalog.Bicycle catalog;

    @BeforeEach
    void buildTheBicycle() {
        catalog = ProductCatalog.cityBicycle();
    }

    @Test
    @DisplayName("A purchased part answers from its own two fields")
    void aPartAnswersForItself() {
        Part spoke = catalog.spoke();

        assertEquals(Money.of(0.40), spoke.totalCost());
        assertEquals(5, spoke.totalWeightGrams());
        assertEquals(1, spoke.partCount());
        assertFalse(spoke.isAssembly());
    }

    @Test
    @DisplayName("The innermost sub-assembly adds its own cost to its lines")
    void hubRollsUp() {
        Assembly hub = catalog.hub();

        // labor 3.00 + axle 6.50 + 2 bearings at 2.10
        assertEquals(Money.of(13.70), hub.totalCost());
        // own 20 g + axle 120 g + 2 x 15 g
        assertEquals(170, hub.totalWeightGrams());
        assertEquals(3, hub.partCount()); // axle + 2 bearings
        assertTrue(hub.isAssembly());
    }

    @Test
    @DisplayName("A sub-assembly that contains another sub-assembly rolls up through it")
    void wheelRollsUpThroughTheHub() {
        Assembly wheel = catalog.wheel();

        // 12.00 labor + rim 24.00 + 32 spokes at 0.40 + hub 13.70 + tire 18.00 + tube 4.50
        assertEquals(Money.of(85.00), wheel.totalCost());
        // 850 + 160 + 170 + 260 + 95
        assertEquals(1535, wheel.totalWeightGrams());
        // rim + 32 spokes + 3 in the hub + tire + tube
        assertEquals(38, wheel.partCount());
    }

    @Test
    @DisplayName("The finished product rolls up the whole four-level structure")
    void bicycleRollsUpEverything() {
        Assembly bicycle = catalog.bicycle();

        // 40.00 final assembly + frame 168.00 + 2 wheels at 85.00 + saddle 18.00
        assertEquals(Money.of(396.00), bicycle.totalCost());
        // frame 2570 + 2 x 1535 + saddle 310
        assertEquals(5950, bicycle.totalWeightGrams());
        // frame 3 + 2 x 38 + saddle 1
        assertEquals(80, bicycle.partCount());
    }

    @Test
    @DisplayName("The frame's own weight and cost are included, not just its parts'")
    void anAssemblyOwnContributionIsCounted() {
        Assembly frame = catalog.frame();

        assertEquals(Money.of(168.00), frame.totalCost()); // 25 welding + 95 + 42 + 6
        assertEquals(2570, frame.totalWeightGrams());      // 30 own + 1800 + 700 + 40
        assertEquals(Money.of(25.00), frame.assemblyCost());
    }

    @Test
    @DisplayName("An empty assembly costs only what it costs to build")
    void anEmptyAssemblyIsTheBaseCase() {
        Assembly empty = new Assembly("EMPTY", "Nothing In It", Money.of(7.50), 12);

        assertEquals(Money.of(7.50), empty.totalCost());
        assertEquals(12, empty.totalWeightGrams());
        assertEquals(0, empty.partCount());
        assertEquals(0, empty.lines().size());
    }
}
