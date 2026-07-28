package dev.kaldiroglu.dp.structural.composite.bom.problem;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The naive design, tested twice over: first that it <b>works</b>, then that
 * working is not the same as being a good design.
 *
 * <p>The first two tests are the fair part — the naive code computes exactly the
 * figures the Composite version computes, so nobody can dismiss it as a straw
 * man. Everything after that measures what it costs to keep those answers
 * right.</p>
 */
class NaiveBomTest {

    private NaiveProductCatalog.Bicycle catalog;

    @BeforeEach
    void buildTheBicycle() {
        catalog = NaiveProductCatalog.cityBicycle();
    }

    // --- It works ----------------------------------------------------------

    @Test
    @DisplayName("The naive design computes the correct totals")
    void theNaiveDesignIsCorrect() {
        Assembly bicycle = catalog.bicycle();

        assertEquals(Money.of(396.00), NaiveCosting.totalCost(bicycle));
        assertEquals(5950, NaiveShipping.totalWeightGrams(bicycle));
        assertEquals(80, NaiveCosting.partCount(bicycle));
    }

    @Test
    @DisplayName("It is correct at every level, not only at the root")
    void theNaiveDesignIsCorrectAtEveryLevel() {
        assertEquals(Money.of(13.70), NaiveCosting.totalCost(catalog.hub1()));
        assertEquals(Money.of(85.00), NaiveCosting.totalCost(catalog.wheel1()));
        assertEquals(Money.of(168.00), NaiveCosting.totalCost(catalog.frame()));
        assertEquals(1535, NaiveShipping.totalWeightGrams(catalog.wheel1()));
    }

    // --- What it costs -----------------------------------------------------

    @Test
    @DisplayName("Cost #1: an operation cannot be a method, so its parameter is Object")
    void operationsBecomeStaticFunctionsOverObject() {
        // There is no type to declare, so the client's own variable must be Object
        // and the compiler can no longer tell it anything useful.
        Object anything = catalog.wheel1();
        assertEquals(Money.of(85.00), NaiveCosting.totalCost(anything));

        // And a plain String type-checks perfectly well at the call site.
        assertThrows(IllegalArgumentException.class,
                () -> NaiveCosting.totalCost("a bicycle, surely?"));
    }

    @Test
    @DisplayName("Cost #2: the same recursion exists three times")
    void theSameWalkIsWrittenThreeTimes() {
        Assembly bicycle = catalog.bicycle();

        // Three separate implementations, each with its own instanceof chain and
        // its own pair of loops. They agree today only because they were written
        // carefully; nothing enforces it.
        assertEquals(Money.of(396.00), NaiveCosting.totalCost(bicycle));   // walk 1
        assertEquals(80, NaiveCosting.partCount(bicycle));                 // walk 2
        assertEquals(5950, NaiveShipping.totalWeightGrams(bicycle));       // walk 3
    }

    @Test
    @DisplayName("Cost #3: no quantities, so every query walks 85 list entries")
    void quantitiesBecomeDuplicatedListEntries() {
        // 32 spokes are 32 entries, and the wheel's 39 entries are counted twice
        // because the wheel itself exists twice.
        assertEquals(85, listEntries(catalog.bicycle()));
        // One wheel's parts list: rim + 32 spokes + tire + tube.
        assertEquals(Catalog.SPOKES_PER_WHEEL + 3, catalog.wheel1().parts().size());

        // The Composite version reaches the same totals from 13 line entries.
        // See ComparisonTest.
    }

    @Test
    @DisplayName("Cost #4: the two wheels are different objects and can drift apart")
    void theTwoWheelsAreDifferentObjectsAndCanDriftApart() {
        assertNotSame(catalog.wheel1(), catalog.wheel2());
        assertEquals(NaiveCosting.totalCost(catalog.wheel1()),
                NaiveCosting.totalCost(catalog.wheel2()));

        // An engineering change applied the way a developer naturally would —
        // to "the wheel", of which there are secretly two.
        catalog.wheel1().addPart(new Part(Catalog.SPOKE), 4); // 32 -> 36 spokes

        assertEquals(Money.of(86.60), NaiveCosting.totalCost(catalog.wheel1()));
        assertEquals(Money.of(85.00), NaiveCosting.totalCost(catalog.wheel2())); // untouched!

        // The product is now wrong: it should be $399.20 with both wheels changed.
        assertEquals(Money.of(397.60), NaiveCosting.totalCost(catalog.bicycle()));
    }

    @Test
    @DisplayName("Cost #5: a new kind of item breaks every existing client")
    void aNewKindOfItemBreaksEveryExistingClient() {
        Service coating = new Service(Catalog.POWDER_COATING);

        IllegalArgumentException costFailure = assertThrows(IllegalArgumentException.class,
                () -> NaiveCosting.totalCost(coating));
        assertTrue(costFailure.getMessage().contains("Service"));

        assertThrows(IllegalArgumentException.class,
                () -> NaiveCosting.partCount(coating));
        assertThrows(IllegalArgumentException.class,
                () -> NaiveShipping.totalWeightGrams(coating));

        // Three clients to edit — and Assembly needs a third collection before a
        // Service can even be attached to one. Compare
        // solution.ExtensibilityTest, where the same requirement costs one class
        // and no edits at all.
    }

    /** Counts the child-list entries a full traversal has to walk. */
    private static int listEntries(Assembly assembly) {
        int entries = assembly.parts().size() + assembly.subAssemblies().size();
        for (Assembly sub : assembly.subAssemblies()) {
            entries += listEntries(sub);
        }
        return entries;
    }
}
