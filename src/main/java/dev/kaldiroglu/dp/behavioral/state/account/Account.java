package dev.kaldiroglu.dp.behavioral.state.account;

/**
 * The account forwards each operation to its status object: Active, Overdrawn, Frozen or
 * Closed. Being frozen is a status, so the account keeps no flag for it.
 */
public class Account {

	private int balance;
	private final int overdraftLimit = 1000;

	private AccountStatus status;

	public Account(int balance) {
		this.balance = balance;
		if (balance >= 0)
			status = new Active(this);
		else
			throw new RuntimeException("Initial balance can't be negative!");
	}

	public void deposit(int amount) {
		requirePositive(amount);
		status.deposit(amount);
	}

	public void withdraw(int amount) {
		requirePositive(amount);
		status.withdraw(amount);
	}
	
	public void transfer(int amount) {
		requirePositive(amount);
		status.transfer(amount);
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public int getOverdraftLimit() {
		return overdraftLimit;
	}

	public void closeAccount() {
		status.close();
	}

	/** A negative amount would turn a deposit into a withdrawal, so no state accepts one. */
	private static void requirePositive(int amount) {
		if (amount <= 0)
			throw new IllegalArgumentException("Amount must be positive: " + amount);
	}
}
