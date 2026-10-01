package dev.kaldiroglu.dp.behavioral.command.account.account1.pattern;

public interface TransactionFactory {
	
	public Transaction createWithdraw();
	
	public Transaction createDeposit();

}
