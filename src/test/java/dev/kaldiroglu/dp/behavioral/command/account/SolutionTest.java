package dev.kaldiroglu.dp.behavioral.command.account;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.InsufficientFundsException;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;
import dev.kaldiroglu.dp.behavioral.command.account.solution.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static dev.kaldiroglu.dp.behavioral.command.account.ProblemTest.codeOf;
import static dev.kaldiroglu.dp.behavioral.command.account.ProblemTest.countOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The same teller, with each request an object. Every figure on the Part 3 slides is
 * asserted here.
 */
class SolutionTest {

    private static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/behavioral/command/account/solution/";

    private static Money lira(String amount) {
        return Money.of(amount);
    }

    @Test
    @DisplayName("the reversal, answered: one undo takes back the whole transfer")
    void aTransferIsUndoneAsOne() {
        Account deniz = new Account("Deniz", lira("1000.00"));
        Account emre = new Account("Emre", lira("0.00"));
        Teller teller = new Teller();

        teller.perform(new Transfer(deniz, emre, lira("300.00")));
        assertEquals(lira("700.00"), deniz.balance());
        assertEquals(lira("300.00"), emre.balance());

        teller.undo();

        assertEquals(lira("1000.00"), deniz.balance());
        assertEquals(lira("0.00"), emre.balance());
        assertEquals(List.of("transfer 300.00 Deniz -> Emre", "undo transfer 300.00 Deniz -> Emre"),
                teller.journal(), "one transfer, one line, one undo");
    }

    @Test
    @DisplayName("undo as far back as you like, and redo what you undid")
    void undoAndRedo() {
        Account deniz = new Account("Deniz", lira("1000.00"));
        Teller teller = new Teller();
        teller.perform(new Deposit(deniz, lira("500.00")));
        teller.perform(new Withdraw(deniz, lira("200.00")));

        teller.undo();
        teller.undo();
        assertEquals(lira("1000.00"), deniz.balance());
        assertFalse(teller.canUndo());

        teller.redo();
        assertEquals(lira("1500.00"), deniz.balance());
        assertTrue(teller.canRedo());
    }

    @Test
    @DisplayName("a command remembers what it did: close out 750, undo, and 750 come back")
    void closeOutRemembersWhatItTook() {
        Account deniz = new Account("Deniz", lira("750.00"));
        Teller teller = new Teller();

        teller.perform(new CloseOut(deniz));
        assertEquals(lira("0.00"), deniz.balance());

        teller.undo();
        assertEquals(lira("750.00"), deniz.balance());
        assertEquals("close out Deniz, paid 750.00", teller.journal().getFirst());
    }

    @Test
    @DisplayName("a failed request never reaches the history, so it can never be undone into money")
    void aFailedWithdrawalIsNotRecorded() {
        Account deniz = new Account("Deniz", lira("500.00"));
        Teller teller = new Teller();

        assertThrows(InsufficientFundsException.class,
                () -> teller.perform(new Withdraw(deniz, lira("600.00"))));
        teller.undo();

        assertEquals(lira("500.00"), deniz.balance());
        assertEquals(List.of(), teller.journal());
    }

    @Test
    @DisplayName("a transfer that cannot be paid leaves both accounts as they were")
    void aTransferIsAllOrNothing() {
        Account deniz = new Account("Deniz", lira("100.00"));
        Account emre = new Account("Emre", lira("0.00"));

        assertThrows(InsufficientFundsException.class,
                () -> new Teller().perform(new Transfer(deniz, emre, lira("300.00"))));

        assertEquals(lira("100.00"), deniz.balance());
        assertEquals(lira("0.00"), emre.balance());
    }

    @Test
    @DisplayName("the teller names no operation: no switch, no case, no instanceof")
    void noBranchInTheInvoker() throws Exception {
        String code = codeOf(SOURCE + "Teller.java");

        assertEquals(0, countOf(code, "switch"));
        assertEquals(0, countOf(code, "case "));
        assertEquals(0, countOf(code, "instanceof"));
        for (String operation : List.of("Deposit", "Withdraw", "Transfer", "CloseOut")) {
            assertEquals(0, countOf(code, operation), operation + " is not named in the teller");
        }
    }

    @Test
    @DisplayName("a new operation costs one class: a fee written in this test, undone like the rest")
    void aNewOperationCostsOneClass() {
        Account deniz = new Account("Deniz", lira("1000.00"));
        Teller teller = new Teller();

        Transaction monthlyFee = new Transaction() {
            private final Withdraw charge = new Withdraw(deniz, lira("25.00"));

            public void execute() { charge.execute(); }
            public void undo() { charge.undo(); }
            public String description() { return "monthly fee Deniz"; }
        };

        teller.perform(monthlyFee);
        assertEquals(lira("975.00"), deniz.balance());

        teller.undo();
        assertEquals(lira("1000.00"), deniz.balance());
        assertEquals("undo monthly fee Deniz", teller.journal().getLast());
    }

    @Test
    @DisplayName("standing orders: made in the morning, run at night, undoable tomorrow")
    void requestsCanWait() {
        Account deniz = new Account("Deniz", lira("1000.00"));
        Account landlord = new Account("Landlord", lira("0.00"));
        Account savings = new Account("Savings", lira("0.00"));
        StandingOrders orders = new StandingOrders();

        orders.schedule(new Transfer(deniz, landlord, lira("400.00")));
        orders.schedule(new Transfer(deniz, savings, lira("100.00")));
        orders.schedule(new Deposit(savings, lira("5.00")));

        assertEquals(3, orders.pending());
        assertEquals(lira("1000.00"), deniz.balance(), "nothing has happened yet");

        Teller nightRun = new Teller();
        orders.runThrough(nightRun);

        assertEquals(0, orders.pending());
        assertEquals(lira("500.00"), deniz.balance());
        assertEquals(lira("105.00"), savings.balance());
        assertEquals(3, nightRun.journal().size());

        nightRun.undo();
        assertEquals(lira("100.00"), savings.balance(), "tonight's last order, taken back");
    }

    @Test
    @DisplayName("the arithmetic of the design: one interface, four transactions, one invoker")
    void theArithmetic() throws IOException {
        List<String> classes;
        try (Stream<Path> files = Files.list(Path.of(SOURCE))) {
            classes = files.map(p -> p.getFileName().toString())
                    .filter(n -> n.endsWith(".java"))
                    .map(n -> n.replace(".java", ""))
                    .sorted()
                    .toList();
        }
        long transactions = classes.stream()
                .filter(n -> !List.of("Transaction", "Teller", "StandingOrders").contains(n))
                .count();

        assertEquals(List.of("CloseOut", "Deposit", "StandingOrders", "Teller", "Transaction",
                "Transfer", "Withdraw"), classes);
        assertEquals(4, transactions);
    }

    @Test
    @DisplayName("execute takes no arguments: the request was complete when it was made")
    void theRequestIsComplete() throws Exception {
        assertEquals(0, Transaction.class.getMethod("execute").getParameterCount());
        assertEquals(0, Transaction.class.getMethod("undo").getParameterCount());
        assertEquals(3, Transaction.class.getDeclaredMethods().length);
    }
}
