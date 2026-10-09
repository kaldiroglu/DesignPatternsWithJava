package dev.kaldiroglu.dp.behavioral.command.account.lambda;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.InsufficientFundsException;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;
import dev.kaldiroglu.dp.behavioral.command.account.solution.CloseOut;
import dev.kaldiroglu.dp.behavioral.command.account.solution.Deposit;
import dev.kaldiroglu.dp.behavioral.command.account.solution.Teller;
import dev.kaldiroglu.dp.behavioral.command.account.solution.Transfer;
import dev.kaldiroglu.dp.behavioral.command.account.solution.Withdraw;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.command.account.lambda.Transactions.*;
import static dev.kaldiroglu.dp.behavioral.command.lender.Printed.by;
import static org.junit.jupiter.api.Assertions.*;

/** The account's transactions written as lambdas, against the classes of {@code solution}. */
class LambdaTransactionTest {

    private static Money lira(String amount) {
        return Money.of(amount);
    }

    @Test
    @DisplayName("Main prints the same lines as the version with transaction classes")
    void sameOutputAsTheClasses() {
        assertEquals(by(() -> dev.kaldiroglu.dp.behavioral.command.account.solution.Main.main(new String[0])),
                by(() -> Main.main(new String[0])));
    }

    @Test
    @DisplayName("each lambda transaction writes the same journal line as its class")
    void sameDescriptions() {
        Account deniz = new Account("Deniz", lira("1000.00"));
        Account emre = new Account("Emre", lira("0.00"));
        assertEquals(new Deposit(deniz, lira("5.00")).description(),
                deposit(deniz, lira("5.00")).description());
        assertEquals(new Withdraw(deniz, lira("5.00")).description(),
                withdraw(deniz, lira("5.00")).description());
        assertEquals(new Transfer(deniz, emre, lira("300.00")).description(),
                transfer(deniz, emre, lira("300.00")).description());
        assertEquals(new CloseOut(deniz).description(), closeOut(deniz).description());
    }

    @Test
    @DisplayName("a close-out remembers what it took, in an array the lambdas share")
    void closeOutRemembers() {
        Account deniz = new Account("Deniz", lira("750.00"));
        Teller teller = new Teller();
        teller.perform(closeOut(deniz));
        assertEquals(lira("0.00"), deniz.balance());
        assertEquals("close out Deniz, paid 750.00", teller.journal().getLast());
        teller.undo();
        assertEquals(lira("750.00"), deniz.balance());
    }

    @Test
    @DisplayName("a transfer that cannot be paid changes nothing and is not recorded")
    void aTransferIsAllOrNothing() {
        Account deniz = new Account("Deniz", lira("100.00"));
        Account emre = new Account("Emre", lira("0.00"));
        Teller teller = new Teller();
        assertThrows(InsufficientFundsException.class,
                () -> teller.perform(transfer(deniz, emre, lira("300.00"))));
        assertEquals(lira("100.00"), deniz.balance());
        assertEquals(lira("0.00"), emre.balance());
        assertEquals(List.of(), teller.journal());
    }

    @Test
    @DisplayName("undo and redo work on lambda transactions as on classes")
    void undoAndRedo() {
        Account deniz = new Account("Deniz", lira("1000.00"));
        Teller teller = new Teller();
        teller.perform(deposit(deniz, lira("500.00")));
        teller.perform(withdraw(deniz, lira("200.00")));
        teller.undo();
        teller.undo();
        assertEquals(lira("1000.00"), deniz.balance());
        teller.redo();
        assertEquals(lira("1500.00"), deniz.balance());
    }
}
