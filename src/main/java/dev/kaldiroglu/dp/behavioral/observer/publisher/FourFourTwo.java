package dev.kaldiroglu.dp.behavioral.observer.publisher;

import java.util.List;

public class FourFourTwo extends AbstractPublication {

	
	public FourFourTwo(String name) {
		super(name);
	}

	public void publish(String date) {
		issueDate = date;
		for (Subscriber subscriber : List.copyOf(subscribers))
			subscriber.receive(this);
	}
}