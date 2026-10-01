package dev.kaldiroglu.dp.behavioral.command.account.account2.problem.ex;

public class InsufficentBalanceException extends Exception {
	private static final String MESSAGE = "Not enough balance! Balance: ";

	public InsufficentBalanceException(double balance) {
		super(MESSAGE + balance);
	}
}
