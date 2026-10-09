package dev.kaldiroglu.dp.behavioral.visitor.hw.accountprint;

import java.io.PrintWriter;
import java.util.List;

/**
 * Prints three accounts twice, as text and as HTML. Each format is a visitor that is
 * given its output; the account classes do not print.
 */
public final class Main {

    public static void main(String[] args) {
        List<Account> accounts = List.of(
                new CheckingAccount("Ayse", 1200, 500),
                new SavingsAccount("Deniz", 5000, 3),
                new LoanAccount("Mert", 20000, 900));
        PrintWriter console = new PrintWriter(System.out, true);

        AccountVisitor text = new TextPrinter(console);
        accounts.forEach(account -> account.accept(text));

        AccountVisitor html = new HtmlPrinter(console);
        accounts.forEach(account -> account.accept(html));
        console.flush();
    }
}
