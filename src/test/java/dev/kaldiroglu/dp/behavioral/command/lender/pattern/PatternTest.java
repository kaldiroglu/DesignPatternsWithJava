package dev.kaldiroglu.dp.behavioral.command.lender.pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.command.lender.Printed.by;
import static dev.kaldiroglu.dp.behavioral.command.lender.Printed.lendParameters;
import static org.junit.jupiter.api.Assertions.*;

/** Step three: the lender runs any command. */
class PatternTest {

    @Test
    @DisplayName("lend takes a Command, and the lender names no concrete command")
    void lendNamesOnlyCommand() {
        assertEquals(List.of(Command.class, int.class), lendParameters(Lender.class));
        for (Method m : Lender.class.getDeclaredMethods()) {
            for (Class<?> type : m.getParameterTypes()) {
                assertNotEquals(Borrower.class, type);
                assertNotEquals(TaxOffice.class, type);
            }
        }
    }

    @Test
    @DisplayName("the money reaches the command at execute time")
    void theAmountArrivesAtExecute() {
        List<Integer> received = new ArrayList<>();
        Command recorder = received::add;
        Lender lender = new Lender();
        lender.lend(recorder, 1000);
        lender.lend(recorder, 2000);
        assertEquals(List.of(1000, 2000), received);
    }

    @Test
    @DisplayName("a borrower and a tax office are both commands")
    void twoCommands() {
        Lender lender = new Lender();
        assertEquals(List.of(
                "Borrowing 1000 and spending for family!",
                "Receiving for the tax payment!"),
                by(() -> {
                    lender.lend(new Borrower(), 1000);
                    lender.lend(new TaxOffice(), 2000);
                }));
    }

    @Test
    @DisplayName("the tax office does not use the amount it is given")
    void theTaxOfficeIgnoresTheAmount() {
        List<String> lines = by(() -> new TaxOffice().execute(2000));
        assertEquals(1, lines.size());
        assertFalse(lines.getFirst().contains("2000"));
    }

    @Test
    @DisplayName("Main prints a loan and a tax payment")
    void mainOutput() {
        assertEquals(List.of(
                "Borrowing 1000 and spending for family!",
                "Receiving for the tax payment!"),
                by(() -> Main.main(new String[0])));
    }
}
