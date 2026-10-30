package dev.kaldiroglu.dp.behavioral.observer.payment;

import java.util.Observable;
import java.util.Observer;

public class Accountant implements Observer {

	public void update(Observable observable, Object arg) {
		System.out.println("\nAccountant has received an update.");
		Invoice invoice = (Invoice) observable;
		System.out.println("Invoice: " + invoice);
	}
}
