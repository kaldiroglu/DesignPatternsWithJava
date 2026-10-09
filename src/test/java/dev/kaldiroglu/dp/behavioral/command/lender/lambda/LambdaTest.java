package dev.kaldiroglu.dp.behavioral.command.lender.lambda;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

import static dev.kaldiroglu.dp.behavioral.command.lender.Printed.by;
import static dev.kaldiroglu.dp.behavioral.command.lender.Printed.lendParameters;
import static org.junit.jupiter.api.Assertions.*;

/** The lender with the command as a function: a lambda is the borrower. */
class LambdaTest {

    @Test
    @DisplayName("lend takes an IntConsumer, the JDK's function from an amount to nothing")
    void lendTakesAFunction() {
        assertEquals(List.of(IntConsumer.class, int.class), lendParameters(Lender.class));
    }

    @Test
    @DisplayName("a lambda receives the money at the moment of lending")
    void aLambdaIsTheCommand() {
        List<Integer> received = new ArrayList<>();
        Lender lender = new Lender();
        lender.lend(received::add, 1000);
        lender.lend(received::add, 2000);
        assertEquals(List.of(1000, 2000), received);
    }

    @Test
    @DisplayName("Main prints the borrower's line, and the tax office's line with the amount it received")
    void mainPrintsTheTwoLines() {
        assertEquals(List.of("Borrowing 1000 and spending for family!",
                "Receiving for the tax payment: 2000"), by(() -> Main.main(new String[0])));
    }
}
