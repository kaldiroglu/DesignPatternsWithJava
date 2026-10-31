
package dev.kaldiroglu.dp.creational.prototype.account.staticFactory;

import dev.kaldiroglu.dp.creational.prototype.account.customer.Customer;

public class Test {

	public static void main(String[] args) {
		// Normal credit account
		Account1 accountInNormalState = Account1.createNormalAccount("1", 1000, 1000, new Customer("Ali"));
		System.out.println("Account in frozen state: " + accountInNormalState);

		// Negative credit account
		Account1 accountInNegativeState = Account1.createNegativeAccount("2", -500, 1000, new Customer("Zeynep"));
		System.out.println("Account in frozen state: " + accountInNegativeState);

		// Frozen account
		Account1 accountInFrozenState =  Account1.createFrozenAccount("3", -1000, 1000, new Customer("Metin"));
		System.out.println("Account in frozen state: " + accountInFrozenState);
	}
}
