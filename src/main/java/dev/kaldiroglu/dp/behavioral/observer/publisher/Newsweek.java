package dev.kaldiroglu.dp.behavioral.observer.publisher;

import java.util.List;

public class Newsweek extends AbstractPublication {
	
	public Newsweek(String name) {
		super(name);
	}
	
	public void publish(String date) {
		issueDate = date;
		for (Subscriber subscriber : List.copyOf(subscribers))
			subscriber.receive(this);
	}
}
