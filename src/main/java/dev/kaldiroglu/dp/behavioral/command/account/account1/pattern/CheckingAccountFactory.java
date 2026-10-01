package dev.kaldiroglu.dp.behavioral.command.account.account1.pattern;

public class CheckingAccountFactory extends AccountFactory {
	private static CheckingAccountFactory factory = new CheckingAccountFactory();

	@Override
	public Account createAccount(int amount) {
		return new CheckingAccount(amount);
	}

	public static CheckingAccountFactory getInstance() {
		return factory;
	}
}
