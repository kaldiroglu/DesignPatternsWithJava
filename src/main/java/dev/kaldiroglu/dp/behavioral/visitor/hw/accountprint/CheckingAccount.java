package dev.kaldiroglu.dp.behavioral.visitor.hw.accountprint;

public record CheckingAccount(String owner, int balance, int overdraftLimit) implements Account {

    @Override
    public void accept(AccountVisitor visitor) {
        visitor.visit(this);
    }
}
