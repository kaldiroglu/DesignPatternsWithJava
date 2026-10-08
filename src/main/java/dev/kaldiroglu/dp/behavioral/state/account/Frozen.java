package dev.kaldiroglu.dp.behavioral.state.account;

public class Frozen implements AccountStatus {
	private Account account;
	
	public Frozen(Account account) {
		this.account = account;
		System.out.println("Status: Frozen and balance: " + account.getBalance());
	}

	@Override
	public void withdraw(int amount) {
		System.out.println("In frozen state no withdraw is allowed!");
	}

	@Override
	public void deposit(int amount) {
		int balance = account.getBalance();
		balance += amount;
		account.setBalance(balance);
		if (balance >= 0)
			account.setStatus(new Active(account));
		else if (balance > -account.getOverdraftLimit())
			account.setStatus(new Overdrawn(account));
		else
			System.out.println("Status: Frozen and balance: " + account.getBalance());
	}

	@Override
	public void transfer(int amount) {
		System.out.println("In frozen state no transfer is allowed!");
	}

	@Override
	public void close() {
		System.out.println("In frozen state the  account can't be closed!");		
	}

}
