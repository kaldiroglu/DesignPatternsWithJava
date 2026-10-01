
package dev.kaldiroglu.dp.behavioral.command.account.account1.problem;

public class NotEnoughBalanceException extends Exception {
	private static String description = "Account does not have enough balance: Balance: ";
	
	public NotEnoughBalanceException(int balance, int amount){
		super(description + balance + " Amount: " + amount);
	}

}
