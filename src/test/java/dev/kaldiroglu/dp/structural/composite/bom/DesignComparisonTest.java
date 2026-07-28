package dev.kaldiroglu.dp.structural.composite.bom;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;
import dev.kaldiroglu.dp.structural.composite.bom.problem.NaiveCosting;
import dev.kaldiroglu.dp.structural.composite.bom.problem.NaiveProductCatalog;
import dev.kaldiroglu.dp.structural.composite.bom.problem.NaiveShipping;
import dev.kaldiroglu.dp.structural.composite.bom.solution.BomComponent;
import dev.kaldiroglu.dp.structural.composite.bom.solution.BomLine;
import dev.kaldiroglu.dp.structural.composite.bom.solution.ProductCatalog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * The two designs, side by side — the test that turns the two packages into one
 * lesson.
 *
 * <p>The first test is the fair one: both designs are built from the same figures
 * in {@link Catalog} and reach <b>identical</b> answers. The naive design is not
 * broken and is not a straw man, and no argument for Composite is allowed to rest
 * on pretending otherwise.</p>
 *
 * <p>The rest measure the difference that <em>is</em> real: how much structure
 * each design needs to reach those answers, and what happens when the world
 * changes.</p>
 */
class DesignComparisonTest {

    private NaiveProductCatalog.Bicycle naive;
    private ProductCatalog.Bicycle composite;

    @BeforeEach
    void buildBothBicycles() {
        naive = NaiveProductCatalog.cityBicycle();
        composite = ProductCatalog.cityBicycle();
    }

    @Test
    @DisplayName("Both designs give exactly the same answers")
    void bothDesignsAgree() {
        assertEquals(NaiveCosting.totalCost(naive.bicycle()),
                composite.bicycle().totalCost());
        assertEquals(NaiveShipping.totalWeightGrams(naive.bicycle()),
                composite.bicycle().totalWeightGrams());
        assertEquals(NaiveCosting.partCount(naive.bicycle()),
                composite.bicycle().partCount());

        // And, for the record, what those answers are.
        assertEquals(Money.of(396.00), composite.bicycle().totalCost());
        assertEquals(5950, composite.bicycle().totalWeightGrams());
        assertEquals(80, composite.bicycle().partCount());
    }

    @Test
    @DisplayName("The naive walk visits 85 child entries; the Composite walk visits 13")
    void theCompositeStructureIsFarSmallerToTraverse() {
        assertEquals(85, naiveListEntries(naive.bicycle()));
        assertEquals(13, compositeLines(composite.bicycle()));
    }

    @Test
    @DisplayName("The naive design needs two wheel objects; the Composite design needs one")
    void onlyTheCompositeDesignCanShare() {
        assertNotSame(naive.wheel1(), naive.wheel2());

        // In the Composite structure there is one wheel, reached by one line with
        // quantity 2.
        BomLine wheelLine = composite.bicycle().lines().stream()
                .filter(line -> line.component().partNumber().equals("WHEEL-ASM"))
                .findFirst()
                .orElseThrow();
        assertSame(composite.wheel(), wheelLine.component());
        assertEquals(Catalog.WHEELS_PER_BICYCLE, wheelLine.quantity());
    }

    @Test
    @DisplayName("The same engineering change is right in one design and wrong in the other")
    void anEngineeringChangeDivergesBetweenTheDesigns() {
        // "Use 36 spokes per wheel instead of 32."
        naive.wheel1().addPart(
                new dev.kaldiroglu.dp.structural.composite.bom.problem.Part(Catalog.SPOKE), 4);
        composite.wheel().changeQuantity(composite.spoke(), 36);

        // The Composite design changes both wheels, because there is one wheel.
        assertEquals(Money.of(399.20), composite.bicycle().totalCost());

        // The naive design changed the wheel the developer happened to reach for,
        // and the product is now quietly wrong by one wheel's worth of spokes.
        assertEquals(Money.of(397.60), NaiveCosting.totalCost(naive.bicycle()));

        // Getting it right in the naive design means remembering the second wheel.
        naive.wheel2().addPart(
                new dev.kaldiroglu.dp.structural.composite.bom.problem.Part(Catalog.SPOKE), 4);
        assertEquals(Money.of(399.20), NaiveCosting.totalCost(naive.bicycle()));
    }

    /** The naive structure: every entry of every child list, recursively. */
    private static int naiveListEntries(dev.kaldiroglu.dp.structural.composite.bom.problem.Assembly assembly) {
        int entries = assembly.parts().size() + assembly.subAssemblies().size();
        for (var sub : assembly.subAssemblies()) {
            entries += naiveListEntries(sub);
        }
        return entries;
    }

    /** The Composite structure: every BomLine, recursively, counted once. */
    private static int compositeLines(BomComponent component) {
        int lines = component.lines().size();
        for (BomLine line : component.lines()) {
            lines += compositeLines(line.component());
        }
        return lines;
    }
}
