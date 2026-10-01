package dev.kaldiroglu.dp.behavioral.command.account.account2.pattern.ex;

public class InsufficientBalanceException extends Exception {
	private static final String MESSAGE = "Not enough balance! Balance: ";

	public InsufficientBalanceException(double balance) {
		super(MESSAGE + balance);
	}
}
