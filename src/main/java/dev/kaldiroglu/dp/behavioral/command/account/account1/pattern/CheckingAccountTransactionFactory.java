package dev.kaldiroglu.dp.behavioral.command.account.account1.pattern;

public class CheckingAccountTransactionFactory implements TransactionFactory {
	private static CheckingAccountTransactionFactory factory = new CheckingAccountTransactionFactory();

	
	public static CheckingAccountTransactionFactory getInstance(){
		return factory;
	}
	
	@Override
	public Transaction createWithdraw() {
		return new Withdraw();
	}
	
	@Override
	public Transaction createDeposit() {
		return new Deposit();
	}

}
