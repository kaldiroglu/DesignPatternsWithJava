package dev.kaldiroglu.dp.behavioral.command.account.account1.pattern;

import java.util.List;

public interface Transaction {

	public void execute(int amount);
	
	public void undo();
	
	public void redo();
	
	public List<Transaction> getTransactions();
	
	public void setAccount(Account account);
}
