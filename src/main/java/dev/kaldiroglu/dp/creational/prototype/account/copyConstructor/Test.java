//
//package dev.kaldiroglu.dp.creational.prototype.account.copyConstructor;
//
//import dev.kaldiroglu.dp.creational.prototype.account.customer.Customer;
//
//public class Test {
//
//	public static void main(String[] args) {
//		// A credit account in normal state
//		Account accountInNormalState = new Account("1", 1000, 1000, new Customer("Ali"), true, true, true);
//		System.out.println("Account in normal state: " + accountInNormalState);
//
//		// Another credit account in normal state. Credit amount is fixed at 1000.
////		Account anotherAccountInNormalState = accountInNormalState.clone();
//		// Change some of its state
//		anotherAccountInNormalState.setIban("2");
//		anotherAccountInNormalState.setBalance(2000);
//		anotherAccountInNormalState.setOwner(new Customer("Remzi"));
//		System.out.println("Account in normal state: " + anotherAccountInNormalState);
//
//		// Account in negative state
//		Account accountInNegativeState = accountInNormalState.clone();
//		// Change some of its state
//		accountInNegativeState.setIban("4");
//		accountInNegativeState.setBalance(-600);
//		accountInNegativeState.setOwner(new Customer("Mustafa"));
//		// Arrange actions
//		accountInNegativeState.setOpenToWithdraw(true);
//		accountInNegativeState.setOpenToPayment(false);
//		accountInNegativeState.setOpenToTransfer(false);
//		System.out.println("Account in negative state: " + accountInNegativeState);
//
//		// Account in frozen state
//		Account accountInFrozenState = accountInNormalState.clone();
//		// Change some of its state
//		accountInFrozenState.setIban("5");
//		accountInFrozenState.setBalance(-1_000);
//		accountInFrozenState.setOwner(new Customer("Sukran"));
//		// Arrange actions
//		accountInFrozenState.setOpenToWithdraw(false);
//		accountInFrozenState.setOpenToPayment(false);
//		accountInFrozenState.setOpenToTransfer(false);
//		System.out.println("Account in frozen state: " + accountInFrozenState);
//	}
//}
