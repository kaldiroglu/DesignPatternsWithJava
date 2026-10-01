package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.ex;

public class AccountNotFoundException extends Exception{
    private static final String MESSAGE = "Not account found with id:: ";

    public AccountNotFoundException(int id) {
        super(MESSAGE + id);
    }
}
