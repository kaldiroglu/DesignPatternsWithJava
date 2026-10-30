package dev.kaldiroglu.dp.behavioral.observer.payment;

import java.util.Observer;

public class Main {

	public static void main(String[] args) {
		Invoice invoice1 = new Invoice(10_000);
		
		Observer boss = new Boss();
		Observer accountant = new Accountant();
		
		invoice1.addObserver(boss);
		invoice1.addObserver(accountant);
		
		invoice1.payBalance(5000);
		invoice1.deleteObserver(boss);
		
		invoice1.payBalance(2000);
	}
}
