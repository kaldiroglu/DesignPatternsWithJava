package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * GoF's third consequence (p. 166), as a runnable claim: <em>"Newly defined
 * Composite or Leaf subclasses work automatically with existing structures and
 * existing client code."</em>
 *
 * <p>{@link Service} is a new kind of Leaf — a subcontracted operation that costs
 * money, weighs nothing, and is not a part. Adding it required writing that one
 * class. Nothing else in the package changed, and none of the operations
 * exercised below were told it exists.</p>
 *
 * <p>The mirror image is {@code problem.NaiveBomTest.aNewKindOfItemBreaksEveryExistingClient},
 * where the same requirement breaks three clients and needs a third collection on
 * the assembly.</p>
 */
class ExtensibilityTest {

    private ProductCatalog.Bicycle catalog;
    private Service coating;

    @BeforeEach
    void buildTheBicycle() {
        catalog = ProductCatalog.cityBicycle();
        coating = new Service(Catalog.POWDER_COATING); // $14.00, 0 g, 0 parts
    }

    @Test
    @DisplayName("A Service answers the Component questions in its own way")
    void aServiceIsALeafWithDifferentAnswers() {
        assertEquals(Money.of(14.00), coating.totalCost());
        assertEquals(0, coating.totalWeightGrams()); // an operation adds no mass
        assertEquals(0, coating.partCount());        // and is not a purchasable part
        assertFalse(coating.isAssembly());
    }

    @Test
    @DisplayName("An existing Assembly accepts it with no change to Assembly")
    void anExistingCompositeAcceptsTheNewLeaf() {
        Assembly frame = catalog.frame();
        frame.add(coating);

        assertEquals(Money.of(182.00), frame.totalCost());  // 168.00 + 14.00
        assertEquals(2570, frame.totalWeightGrams());       // unchanged
        assertEquals(3, frame.partCount());                 // unchanged
    }

    @Test
    @DisplayName("The roll-up above it is correct without anyone editing the roll-up")
    void theRollUpAbsorbsItAutomatically() {
        Assembly bicycle = catalog.bicycle();
        bicycle.totalCost(); // warm the caches first, to prove invalidation still works

        catalog.frame().add(coating);

        assertEquals(Money.of(410.00), bicycle.totalCost()); // 396.00 + 14.00
        assertEquals(5950, bicycle.totalWeightGrams());      // unchanged
        assertEquals(80, bicycle.partCount());               // unchanged
    }

    @Test
    @DisplayName("Clients written before the class existed handle it correctly")
    void preExistingClientsKeepWorking() {
        catalog.frame().add(coating);

        // The tree printer: never edited, renders the new leaf.
        String tree = catalog.bicycle().toTree();
        assertTrue(tree.contains("Powder Coating [SVC-COAT]"));

        // A shipping estimator written against BomComponent: still right, because
        // a service contributes no mass.
        assertEquals(Money.of(29.40), shippingEstimate(catalog.bicycle()));

        // And a generic recursive walk still terminates, because a Service is a
        // leaf and reports no lines.
        assertEquals(15, countNodes(catalog.bicycle())); // 14 before, plus the service
    }

    @Test
    @DisplayName("It nests like any other component")
    void aServiceCanAppearAnywhereInTheStructure() {
        catalog.hub().add(coating); // four levels down

        assertEquals(Money.of(27.70), catalog.hub().totalCost());     // 13.70 + 14.00
        assertEquals(Money.of(99.00), catalog.wheel().totalCost());   // 85.00 + 14.00
        // Two wheels, so the product picks it up twice.
        assertEquals(Money.of(424.00), catalog.bicycle().totalCost()); // 396.00 + 28.00
    }

    /** The same estimator the demo uses — written against the Component only. */
    private static Money shippingEstimate(BomComponent component) {
        int kilos = Math.max(1, (component.totalWeightGrams() + 999) / 1000);
        return Money.of(4.90).times(kilos);
    }

    private static int countNodes(BomComponent component) {
        int count = 1;
        for (BomLine line : component.lines()) {
            count += countNodes(line.component());
        }
        return count;
    }
}
