package dev.kaldiroglu.dp.behavioral.visitor.hw.accountprint;

import dev.kaldiroglu.dp.behavioral.visitor.Printed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Homework 1: each printer is a visitor, and it is given the output it writes to. */
class AccountPrintTest {

    private static final List<Account> ACCOUNTS = List.of(
            new CheckingAccount("Ayse", 1200, 500),
            new SavingsAccount("Deniz", 5000, 3),
            new LoanAccount("Mert", 20000, 900));

    private static List<String> printedBy(java.util.function.Function<PrintWriter, AccountVisitor> printer) {
        StringWriter written = new StringWriter();
        PrintWriter out = new PrintWriter(written);
        AccountVisitor visitor = printer.apply(out);
        ACCOUNTS.forEach(account -> account.accept(visitor));
        out.flush();
        return written.toString().lines().toList();
    }

    @Test
    @DisplayName("the text printer writes one line per account to the output it is given")
    void textToAGivenOutput() {
        assertEquals(List.of(
                "Checking Ayse: 1200 (overdraft 500)",
                "Savings  Deniz: 5000 (3% interest)",
                "Loan     Mert: owes 20000, pays 900 a month"),
                printedBy(TextPrinter::new));
    }

    @Test
    @DisplayName("the HTML printer writes one table row per account; a loan is a negative amount")
    void htmlToAGivenOutput() {
        assertEquals(List.of(
                "<tr><td>Checking</td><td>Ayse</td><td>1200</td></tr>",
                "<tr><td>Savings</td><td>Deniz</td><td>5000</td></tr>",
                "<tr><td>Loan</td><td>Mert</td><td>-20000</td></tr>"),
                printedBy(HtmlPrinter::new));
    }

    @Test
    @DisplayName("the accounts do not print: Account has only owner and accept")
    void theAccountsDoNotPrint() {
        assertEquals(List.of("accept", "owner"),
                Arrays.stream(Account.class.getDeclaredMethods()).map(Method::getName).sorted().toList());
    }

    @Test
    @DisplayName("Main prints the three accounts as text, then as HTML")
    void mainOutput() {
        assertEquals(List.of(
                "Checking Ayse: 1200 (overdraft 500)",
                "Savings  Deniz: 5000 (3% interest)",
                "Loan     Mert: owes 20000, pays 900 a month",
                "<tr><td>Checking</td><td>Ayse</td><td>1200</td></tr>",
                "<tr><td>Savings</td><td>Deniz</td><td>5000</td></tr>",
                "<tr><td>Loan</td><td>Mert</td><td>-20000</td></tr>"),
                Printed.by(() -> Main.main(new String[0])));
    }
}
