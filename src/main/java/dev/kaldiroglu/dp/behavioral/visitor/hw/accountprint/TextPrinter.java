package dev.kaldiroglu.dp.behavioral.visitor.hw.accountprint;

import java.io.PrintWriter;

/** A <b>ConcreteVisitor</b> that writes one plain line per account to the output it is given. */
public final class TextPrinter implements AccountVisitor {

    private final PrintWriter out;

    public TextPrinter(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void visit(CheckingAccount account) {
        out.println("Checking " + account.owner() + ": " + account.balance()
                + " (overdraft " + account.overdraftLimit() + ")");
    }

    @Override
    public void visit(SavingsAccount account) {
        out.println("Savings  " + account.owner() + ": " + account.balance()
                + " (" + account.interestPercent() + "% interest)");
    }

    @Override
    public void visit(LoanAccount account) {
        out.println("Loan     " + account.owner() + ": owes " + account.debt()
                + ", pays " + account.monthlyPayment() + " a month");
    }
}
