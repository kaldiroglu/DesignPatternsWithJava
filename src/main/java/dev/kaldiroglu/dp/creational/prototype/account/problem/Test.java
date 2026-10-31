
package dev.kaldiroglu.dp.creational.prototype.account.problem;

import dev.kaldiroglu.dp.creational.prototype.account.customer.Customer;

public class Test {

	public static void main(String[] args) {
		// Normal credit account
		Account normalAccount = new Account("1", 1000, 1000, new Customer("Ali"), true, true, true);
		System.out.println("Account in normal state: " + normalAccount);

		// Negative credit account
		Account negativeAccount = new Account("2", -500, 1000, new Customer("Zeynep"), true, true, false);
		System.out.println("Account in negative state: " + negativeAccount);

		// Frozen account
		Account frozenAccount = new Account("3", -1000, 1000, new Customer("Metin"), false, false, false);
		System.out.println("Account in frozen state: " + frozenAccount);
	}
}
