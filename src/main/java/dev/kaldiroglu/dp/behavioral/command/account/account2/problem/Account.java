package dev.kaldiroglu.dp.behavioral.command.account.account2.problem;

public class Account {

	private double balance;

	public Account(int balance) {
		this.balance = balance;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

//	public void deposit(int amount) {
//		balance += amount;
//	}
//
//	public void withdraw(int amount) throws NotEnoughBalanceException {
//		if (amount <= balance)
//			balance -= amount;
//		else
//			throw new NotEnoughBalanceException(balance, amount);
//	}

	@Override
	public String toString() {
		return "Account [balance=" + balance + "]";
	}
}
