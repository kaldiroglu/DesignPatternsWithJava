package dev.kaldiroglu.dp.behavioral.visitor.hw.accountprint;

public record SavingsAccount(String owner, int balance, int interestPercent) implements Account {

    @Override
    public void accept(AccountVisitor visitor) {
        visitor.visit(this);
    }
}
