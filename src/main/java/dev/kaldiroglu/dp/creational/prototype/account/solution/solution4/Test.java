
package dev.kaldiroglu.dp.creational.prototype.account.solution.solution4;

import dev.kaldiroglu.dp.creational.prototype.account.customer.Customer;

public class Test {
	private static Factory factory = new AccountFactory();
	
	public static void main(String[] args) {
		
		// Normal account. Credit amount is fixed at 1000.
		Account normalAccount = factory.createNormalAccount();
		normalAccount.setIban("2");
		normalAccount.setBalance(2000);
		normalAccount.setOwner(new Customer("Remzi"));
		System.out.println("Account in normal state: " + normalAccount);

		// Negative account
		Account negativeAccount = factory.createNegativeAccount(new Customer("Mustafa"), -750);
		System.out.println("Account in negative state: " + negativeAccount);

		// Frozen default account
		Account frozenAccount = factory.createFrozenAccount(new Customer("Sukran"), -1000);
		System.out.println("Account in frozen state: " + frozenAccount);
	}
}
