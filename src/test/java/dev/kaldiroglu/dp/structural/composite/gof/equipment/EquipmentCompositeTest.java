package dev.kaldiroglu.dp.structural.composite.gof.equipment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the equipment example of GoF pp. 170–173.
 *
 * <p>The fixture is the book's own assembly: a cabinet holding a chassis, which
 * holds a bus with one card plus a floppy disk.</p>
 */
class EquipmentCompositeTest {

    private Cabinet cabinet;
    private Chassis chassis;
    private Bus bus;
    private Card card;
    private FloppyDisk floppy;

    @BeforeEach
    void assembleThePc() {
        cabinet = new Cabinet("PC Cabinet");          // own net  $90.00,  0 W
        chassis = new Chassis("PC Chassis");          // own net $210.00, 25 W
        bus = new Bus("MCA Bus");                     // own net  $75.00, 10 W
        card = new Card("16Mbs Token Ring");          //     net $120.00,  8 W
        floppy = new FloppyDisk("3.5in Floppy");      //     net  $35.00, 15 W

        cabinet.add(chassis);
        bus.add(card);
        chassis.add(bus);
        chassis.add(floppy);
    }

    @Test
    @DisplayName("A leaf answers from its own state")
    void leafPricesItself() {
        assertEquals(Currency.of(120.00), card.netPrice());
        assertEquals(8, card.power());
    }

    @Test
    @DisplayName("netPrice() on a composite sums its own price and its subtree")
    void netPriceRollsUpThroughTheTree() {
        // bus = 75 + card 120
        assertEquals(Currency.of(195.00), bus.netPrice());
        // chassis = 210 + bus 195 + floppy 35
        assertEquals(Currency.of(440.00), chassis.netPrice());
        // cabinet = 90 + chassis 440
        assertEquals(Currency.of(530.00), cabinet.netPrice());
    }

    @Test
    @DisplayName("power() rolls up the same way")
    void powerRollsUpThroughTheTree() {
        assertEquals(18, bus.power());       // 10 + 8
        assertEquals(58, chassis.power());   // 25 + 18 + 15
        assertEquals(58, cabinet.power());   // 0 + 58 — the cabinet draws nothing itself
    }

    @Test
    @DisplayName("discountPrice() applies each node's own rate as it rolls up")
    void discountPriceRollsUpWithPerNodeRates() {
        // card 120 * 0.95 = 114.00; bus own 75 * 0.90 = 67.50
        assertEquals(Currency.of(181.50), bus.discountPrice());
        // chassis own 210 * 0.85 = 178.50; + bus 181.50; + floppy 35 * 0.90 = 31.50
        assertEquals(Currency.of(391.50), chassis.discountPrice());
        // cabinet own 90 * 0.80 = 72.00; + chassis 391.50
        assertEquals(Currency.of(463.50), cabinet.discountPrice());
    }

    @Test
    @DisplayName("Adding equipment changes every ancestor's answer")
    void addingAPartUpdatesTheWholeTree() {
        Currency before = cabinet.netPrice();
        bus.add(new Card("Ethernet", 6, Currency.of(60.00)));

        assertEquals(before.plus(Currency.of(60.00)), cabinet.netPrice());
        assertEquals(64, cabinet.power()); // 58 + 6
    }

    @Test
    @DisplayName("Removing equipment does the same in reverse")
    void removingAPartUpdatesTheWholeTree() {
        chassis.remove(floppy);

        assertEquals(Currency.of(405.00), chassis.netPrice()); // 440 - 35
        assertEquals(Currency.of(495.00), cabinet.netPrice()); // 530 - 35
    }

    @Test
    @DisplayName("A simple piece of equipment rejects child operations")
    void leavesRejectChildOperations() {
        assertThrows(UnsupportedOperationException.class, () -> card.add(floppy));
        assertThrows(UnsupportedOperationException.class, () -> floppy.remove(card));
        assertTrue(chassis.isComposite());
        assertTrue(cabinet.isComposite());
    }

    @Test
    @DisplayName("Any Equipment is iterable, so one walk covers leaves and assemblies")
    void theWholeTreeIsWalkableThroughTheComponentInterface() {
        assertEquals(5, countNodes(cabinet)); // cabinet, chassis, bus, card, floppy
        assertEquals(1, countNodes(card));    // a leaf is a one-node tree
    }

    private static int countNodes(Equipment equipment) {
        int count = 1;
        for (Equipment part : equipment) {
            count += countNodes(part);
        }
        return count;
    }
}
