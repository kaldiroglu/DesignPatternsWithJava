package dev.kaldiroglu.dp.behavioral.command.account.account1.pattern;

public class AccountFactory implements Factory{
	private static AccountFactory factory = new AccountFactory();

	@Override
	public Account createAccount(int amount){
		return new Account(amount);
	}
	
	public static AccountFactory getInstance() {
		return factory;
	}
}
