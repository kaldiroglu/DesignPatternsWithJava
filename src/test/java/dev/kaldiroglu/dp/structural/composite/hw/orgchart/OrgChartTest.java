package dev.kaldiroglu.dp.structural.composite.hw.orgchart;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** The roll-up is the easy half; the sharing trap is the half worth the exercise. */
class OrgChartTest {

    private final IndividualContributor cem = new IndividualContributor("Cem", "designer", 80_000);
    private final Manager engineering = new Manager("Deniz", "eng manager", 120_000)
            .add(new IndividualContributor("Ayse", "engineer", 90_000))
            .add(new IndividualContributor("Bora", "engineer", 85_000));
    private final Manager design = new Manager("Ece", "design manager", 115_000).add(cem);
    private final Manager chief = new Manager("Fatma", "CTO", 180_000).add(engineering).add(design);

    @Test
    @DisplayName("a leaf answers for itself, a manager for the whole tree")
    void rollUp() {
        assertEquals(1, cem.headcount());
        assertEquals(80_000, cem.totalCost());
        assertEquals(3, engineering.headcount());
        assertEquals(295_000, engineering.totalCost());
        assertEquals(6, chief.headcount());
        assertEquals(670_000, chief.totalCost());
    }

    @Test
    @DisplayName("a manager counts their own salary, or the company has no chief executive")
    void managersCountThemselves() {
        Manager alone = new Manager("Solo", "manager", 100_000);
        assertEquals(1, alone.headcount());
        assertEquals(100_000, alone.totalCost());
    }

    @Test
    @DisplayName("sharing one person between two managers silently over-counts")
    void theSharingTrap() {
        engineering.add(cem);   // Cem now reports to both Ece and Deniz

        assertEquals(7, chief.headcount(), "the tree walk sees Cem twice");
        assertEquals(6, chief.distinctHeadcount(), "identity de-duplication gives the truth");
        assertNotEquals(chief.headcount(), chief.distinctHeadcount());
        assertEquals(750_000, chief.totalCost(), "and his salary is paid twice");
    }

    @Test
    @DisplayName("a cycle is refused, because that one does not merely over-count")
    void cyclesAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> engineering.add(chief));
        assertThrows(IllegalArgumentException.class, () -> chief.add(chief));
    }

    @Test
    @DisplayName("a client never asks which kind of employee it holds")
    void oneTypeForBoth() {
        Employee asComponent = engineering;
        assertTrue(asComponent.render("").contains("Ayse"));
        assertEquals(3, asComponent.headcount());
        assertEquals(1, ((Employee) cem).headcount());
    }
}
