package dev.kaldiroglu.dp.behavioral.visitor.hw.accountprint;

public record LoanAccount(String owner, int debt, int monthlyPayment) implements Account {

    @Override
    public void accept(AccountVisitor visitor) {
        visitor.visit(this);
    }
}
