package dev.kaldiroglu.dp.behavioral.visitor.hw.accountprint;

/** The <b>Visitor</b>: one method for each kind of account. */
public interface AccountVisitor {

    void visit(CheckingAccount account);

    void visit(SavingsAccount account);

    void visit(LoanAccount account);
}
