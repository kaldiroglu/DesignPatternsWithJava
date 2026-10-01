package dev.kaldiroglu.dp.behavioral.command.account.account1.pattern;

public class Deposit extends AbstractTransaction {

	@Override
	public void execute(int amount) {
		this.amount = amount;
		account.deposit(amount);
		account.addTransaction(this);
	}

	@Override
	public void undo() {
		try {
			account.withdraw(amount);
		} catch (NotEnoughBalanceException e) {
			System.out.println("Can't withdraw: " + e.getMessage());
		}

		account.addTransaction(this);
	}
}
