package dev.kaldiroglu.dp.behavioral.observer.payment;

import java.util.Observable;
import java.util.Observer;

public class Boss implements Observer {

	public void update(Observable observable, Object arg) {
		System.out.println("\nBoss has received an update.");
		Invoice invoice = (Invoice) observable;
		System.out.println(invoice);
	}
}