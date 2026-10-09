package dev.kaldiroglu.dp.behavioral.command.lender.problem1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.command.lender.Printed.by;
import static dev.kaldiroglu.dp.behavioral.command.lender.Printed.lendParameters;
import static org.junit.jupiter.api.Assertions.*;

/** Step one: the lender knows one borrower class. */
class Problem1Test {

    @Test
    @DisplayName("the lender passes the money to the borrower")
    void lendsToTheBorrower() {
        assertEquals(List.of("Borrowing 1000 and spending for family!"),
                by(() -> new Lender().lend(new Borrower(), 1000)));
    }

    @Test
    @DisplayName("lend takes the concrete Borrower class, so no other borrower fits")
    void lendNamesTheConcreteClass() {
        assertEquals(List.of(Borrower.class, int.class), lendParameters(Lender.class));
        assertFalse(Borrower.class.isInterface());
    }

    @Test
    @DisplayName("Main prints one loan")
    void mainOutput() {
        assertEquals(List.of("Borrowing 1000 and spending for family!"),
                by(() -> Main.main(new String[0])));
    }
}
