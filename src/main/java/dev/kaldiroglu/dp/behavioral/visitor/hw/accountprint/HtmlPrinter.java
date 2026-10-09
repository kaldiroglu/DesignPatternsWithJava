package dev.kaldiroglu.dp.behavioral.visitor.hw.accountprint;

import java.io.PrintWriter;

/** A <b>ConcreteVisitor</b> that writes one HTML table row per account. */
public final class HtmlPrinter implements AccountVisitor {

    private final PrintWriter out;

    public HtmlPrinter(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void visit(CheckingAccount account) {
        row("Checking", account.owner(), String.valueOf(account.balance()));
    }

    @Override
    public void visit(SavingsAccount account) {
        row("Savings", account.owner(), String.valueOf(account.balance()));
    }

    @Override
    public void visit(LoanAccount account) {
        row("Loan", account.owner(), String.valueOf(-account.debt()));
    }

    private void row(String kind, String owner, String amount) {
        out.println("<tr><td>" + kind + "</td><td>" + owner + "</td><td>" + amount + "</td></tr>");
    }
}
