package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The structural rules of the composite: sharing, editing, cycles, and the fact
 * that a single recursive walk covers parts and assemblies alike.
 */
class StructureTest {

    private ProductCatalog.Bicycle catalog;

    @BeforeEach
    void buildTheBicycle() {
        catalog = ProductCatalog.cityBicycle();
    }

    @Test
    @DisplayName("Two wheels are one shared object, with the quantity on the line")
    void aSubAssemblyIsSharedNotDuplicated() {
        Assembly bicycle = catalog.bicycle();
        BomLine wheelLine = bicycle.lines().stream()
                .filter(line -> line.component().partNumber().equals("WHEEL-ASM"))
                .findFirst()
                .orElseThrow();

        assertSame(catalog.wheel(), wheelLine.component());
        assertEquals(2, wheelLine.quantity());
        // The bicycle has three lines, not four: frame, wheel x2, saddle.
        assertEquals(3, bicycle.lines().size());
    }

    @Test
    @DisplayName("A line's extended figures multiply the child's roll-up by the quantity")
    void extendedFiguresMultiplyByQuantity() {
        BomLine twoWheels = new BomLine(catalog.wheel(), 2);

        assertEquals(Money.of(170.00), twoWheels.extendedCost());
        assertEquals(3070, twoWheels.extendedWeightGrams());
        assertEquals(76, twoWheels.extendedPartCount());
    }

    @Test
    @DisplayName("A shared sub-assembly knows all of its parents")
    void sharedComponentsHaveSeveralParents() {
        Assembly tandemFrame = new Assembly("FRAME-TANDEM", "Tandem Frame");
        tandemFrame.add(catalog.wheel(), 2);

        // The one wheel object is now used by two different products.
        assertEquals(List.of(catalog.bicycle(), tandemFrame), catalog.wheel().parents());
    }

    @Test
    @DisplayName("Adding a component that already appears is rejected")
    void duplicateLinesAreRejected() {
        Assembly bicycle = catalog.bicycle();

        IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
                () -> bicycle.add(catalog.wheel(), 1));
        assertTrue(failure.getMessage().contains("already a line"));
    }

    @Test
    @DisplayName("A component may not contain itself, directly or indirectly")
    void cyclesAreRejected() {
        Assembly bicycle = catalog.bicycle();
        Assembly wheel = catalog.wheel();
        Assembly hub = catalog.hub();

        assertThrows(IllegalArgumentException.class, () -> bicycle.add(bicycle));
        // The bicycle contains the wheel, so the wheel may not contain the bicycle.
        assertThrows(IllegalArgumentException.class, () -> wheel.add(bicycle));
        // Two levels down is just as illegal.
        assertThrows(IllegalArgumentException.class, () -> hub.add(bicycle));
    }

    @Test
    @DisplayName("Removing a line detaches the child and clears its parent link")
    void removeDetachesAChild() {
        Assembly bicycle = catalog.bicycle();
        Assembly wheel = catalog.wheel();

        assertTrue(bicycle.remove(wheel));
        assertEquals(2, bicycle.lines().size());
        assertEquals(List.of(), wheel.parents());
        assertFalse(bicycle.remove(wheel)); // already gone
    }

    @Test
    @DisplayName("A part exposes no lines, so one recursive walk handles every node")
    void aSingleWalkVisitsPartsAndAssembliesAlike() {
        assertEquals(List.of(), catalog.spoke().lines());
        // 1 bicycle + 1 frame + 3 frame parts + 1 wheel + 4 wheel parts
        // + 1 hub + 2 hub parts + 1 saddle = 14 distinct nodes
        assertEquals(14, countNodes(catalog.bicycle()));
        assertEquals(1, countNodes(catalog.spoke()));
    }

    @Test
    @DisplayName("The tree rendering shows quantities and extended figures")
    void treeRenderingIncludesTheWholeStructure() {
        String tree = catalog.bicycle().toTree();

        assertTrue(tree.contains("City Bicycle [BIKE-CITY]"));
        assertTrue(tree.contains("2x 700c Wheel [WHEEL-ASM]"));
        assertTrue(tree.contains("32x 14g Spoke [SPOKE-14G]"));
        assertTrue(tree.contains("$170.00")); // two wheels, extended
    }

    /** Counts distinct nodes, ignoring quantities — the shared wheel counts once. */
    private static int countNodes(BomComponent component) {
        int count = 1;
        for (BomLine line : component.lines()) {
            count += countNodes(line.component());
        }
        return count;
    }
}
