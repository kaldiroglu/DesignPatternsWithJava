package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.ex;

public class IllegalMoneyException extends Exception{
    private static final String MESSAGE = "Value must be greater than zero and less than 1_000_000.";

    public IllegalMoneyException(){
        super(MESSAGE);
    }
}
