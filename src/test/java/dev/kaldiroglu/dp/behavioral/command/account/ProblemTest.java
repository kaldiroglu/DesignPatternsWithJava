package dev.kaldiroglu.dp.behavioral.command.account;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;
import dev.kaldiroglu.dp.behavioral.command.account.problem.HistoryAccount;
import dev.kaldiroglu.dp.behavioral.command.account.problem.Kind;
import dev.kaldiroglu.dp.behavioral.command.account.problem.OneStepAccount;
import dev.kaldiroglu.dp.behavioral.command.account.problem.Teller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The three naive designs, and what each one costs.
 * <p>
 * All three undo correctly for the case they were built for — that is what makes them worth
 * teaching — so every figure the slides quote about them is measured here.
 */
class ProblemTest {

    private static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/behavioral/command/account/problem/";

    private static Money lira(String amount) {
        return Money.of(amount);
    }

    /** The source of a class with every comment removed, so its javadoc cannot match. */
    static String codeOf(String path) throws Exception {
        String text = Files.readString(Path.of(path));
        text = text.replaceAll("(?s)/\\*.*?\\*/", "");
        return text.replaceAll("//[^\\n]*", "");
    }

    static int countOf(String text, String needle) {
        int count = 0;
        for (int i = text.indexOf(needle); i >= 0; i = text.indexOf(needle, i + needle.length())) {
            count++;
        }
        return count;
    }

    private static Set<String> publicMethodsOf(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName)
                .collect(Collectors.toSet());
    }

    // ------------------------------------------- stage one: the account remembers one move

    @Test
    @DisplayName("stage one: one undo works")
    void oneUndoWorks() {
        OneStepAccount account = new OneStepAccount("Deniz", lira("1000.00"));
        account.withdraw(lira("200.00"));
        account.undo();

        assertEquals(lira("1000.00"), account.balance());
    }

    @Test
    @DisplayName("stage one: a second undo finds nothing, because the first one erased it")
    void onlyOneStep() {
        OneStepAccount account = new OneStepAccount("Deniz", lira("1000.00"));
        account.deposit(lira("500.00"));
        account.withdraw(lira("200.00"));

        account.undo();
        account.undo();

        assertEquals(lira("1500.00"), account.balance(), "the deposit can no longer be undone");
    }

    @Test
    @DisplayName("stage one: two of the account's three instance fields are bookkeeping")
    void theAccountCarriesBookkeeping() {
        List<String> fields = Arrays.stream(OneStepAccount.class.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .map(Field::getName)
                .toList();

        assertEquals(List.of("owner", "balance", "lastKind", "lastAmount"), fields);
        assertEquals(2, fields.stream().filter(n -> n.startsWith("last")).count());
    }

    // ------------------------------------------------- stage two: the account keeps a history

    @Test
    @DisplayName("stage two: undo as far back as you like, redo, and a journal")
    void theHistoryWorks() {
        HistoryAccount account = new HistoryAccount("Deniz", lira("1000.00"));
        account.deposit(lira("500.00"));
        account.withdraw(lira("200.00"));

        account.undo();
        account.undo();
        assertEquals(lira("1000.00"), account.balance());

        account.redo();
        assertEquals(lira("1500.00"), account.balance());
        assertEquals(List.of("deposit 500.00", "withdraw 200.00", "undo withdraw 200.00",
                "undo deposit 500.00", "redo deposit 500.00"), account.journal());
    }

    @Test
    @DisplayName("stage two: eight public operations, and three of them are banking")
    void aFamilyOfHelperMethods() {
        Set<String> all = publicMethodsOf(HistoryAccount.class);
        Set<String> banking = publicMethodsOf(Account.class);

        assertEquals(8, all.size());
        assertEquals(3, all.stream().filter(banking::contains).count(),
                "balance, deposit and withdraw are what an account is for");
        assertEquals(Set.of("undo", "redo", "canUndo", "canRedo", "journal"),
                all.stream().filter(m -> !banking.contains(m)).collect(Collectors.toSet()));
    }

    @Test
    @DisplayName("stage two: every operation lives in three places")
    void everyOperationIsThreeEdits() throws Exception {
        String code = codeOf(SOURCE + "HistoryAccount.java");

        assertEquals(2, countOf(code, "switch ("), "one switch to undo, one to redo");
        assertEquals(2 * Kind.values().length, countOf(code, "case "),
                "each kind is a branch in both");
    }

    // ------------------------------------------------ stage three: the history moves out

    @Test
    @DisplayName("stage three: the account is clean again and the teller undoes")
    void theTellerWorks() {
        Account deniz = new Account("Deniz", lira("1000.00"));
        Teller teller = new Teller();
        teller.withdraw(deniz, lira("200.00"));
        teller.deposit(deniz, lira("50.00"));

        teller.undo();
        teller.undo();
        assertEquals(lira("1000.00"), deniz.balance());

        teller.redo();
        assertEquals(lira("800.00"), deniz.balance());
        assertEquals(Set.of("owner", "balance", "deposit", "withdraw"),
                publicMethodsOf(Account.class), "banking and nothing else");
    }

    @Test
    @DisplayName("stage three: the switches have moved into the teller, and there are still two")
    void theSwitchesMoved() throws Exception {
        String code = codeOf(SOURCE + "Teller.java");

        assertEquals(2, countOf(code, "switch ("));
        assertEquals(2 * Kind.values().length, countOf(code, "case "));
    }

    // ---------------------------------------------------------------------- the reversal

    @Test
    @DisplayName("the reversal: undo takes back half a transfer, and 300 lira vanish")
    void theReversal() {
        Account deniz = new Account("Deniz", lira("1000.00"));
        Account emre = new Account("Emre", lira("0.00"));
        Teller teller = new Teller();

        teller.transfer(deniz, emre, lira("300.00"));
        assertEquals(lira("700.00"), deniz.balance());
        assertEquals(lira("300.00"), emre.balance());

        // The teller presses Undo once, to take back the transfer.
        teller.undo();

        assertEquals(lira("700.00"), deniz.balance(), "the withdrawal is still there");
        assertEquals(lira("0.00"), emre.balance(), "the deposit is gone");
        assertEquals(lira("300.00"),
                lira("1000.00").minus(deniz.balance().plus(emre.balance())),
                "money that left one account and arrived nowhere");

        // One transfer, and the journal cannot tell it was one.
        assertEquals(List.of("withdraw 300.00 Deniz", "deposit 300.00 Emre",
                "undo deposit 300.00 Emre"), teller.journal());
    }

    @Test
    @DisplayName("the reversal: transfer adds no branch, which is exactly why it breaks")
    void theTransferReusesAndRecordsTwice() throws Exception {
        String code = codeOf(SOURCE + "Teller.java");
        String transfer = code.substring(code.indexOf("public void transfer"),
                code.indexOf("public void undo"));

        assertEquals(1, countOf(transfer, "withdraw("));
        assertEquals(1, countOf(transfer, "deposit("));
        assertEquals(0, countOf(transfer, "record("), "it records nothing of its own");
    }
}
