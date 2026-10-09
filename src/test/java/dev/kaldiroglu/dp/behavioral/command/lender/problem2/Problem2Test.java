package dev.kaldiroglu.dp.behavioral.command.lender.problem2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.command.lender.Printed.by;
import static dev.kaldiroglu.dp.behavioral.command.lender.Printed.lendParameters;
import static org.junit.jupiter.api.Assertions.*;

/** Step two: the lender knows a borrower interface. */
class Problem2Test {

    @Test
    @DisplayName("lend takes the Borrower interface")
    void lendNamesTheInterface() {
        assertEquals(List.of(Borrower.class, int.class), lendParameters(Lender.class));
        assertTrue(Borrower.class.isInterface());
    }

    @Test
    @DisplayName("any borrower works, even one written in this test")
    void anyBorrowerWorks() {
        List<Integer> received = new ArrayList<>();
        Borrower recorder = received::add;
        new Lender().lend(recorder, 750);
        assertEquals(List.of(750), received);
    }

    @Test
    @DisplayName("the two borrowers spend the money in two ways")
    void twoBorrowers() {
        Lender lender = new Lender();
        assertEquals(List.of(
                "Borrowing 1000 and spending for family!",
                "Borrowing 2000 and spending for school!"),
                by(() -> {
                    lender.lend(new ConcreteBorrower1(), 1000);
                    lender.lend(new ConcreteBorrower2(), 2000);
                }));
    }

    @Test
    @DisplayName("Main prints two loans")
    void mainOutput() {
        assertEquals(List.of(
                "Borrowing 1000 and spending for family!",
                "Borrowing 2000 and spending for school!"),
                by(() -> Main.main(new String[0])));
    }
}
