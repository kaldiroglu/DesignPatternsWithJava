package dev.kaldiroglu.dp.behavioral.command.account.account2.problem.ex;

public class NegativeAmountException extends Exception {
    private static final String MESSAGE = "Amount can not be negative: ";

    public NegativeAmountException(double amount) {
        super(MESSAGE + amount);
    }
}
