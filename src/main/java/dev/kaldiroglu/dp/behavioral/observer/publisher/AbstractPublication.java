package dev.kaldiroglu.dp.behavioral.observer.publisher;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPublication implements Publication{
	
	protected String name;
	protected String issueDate;
	protected List<Subscriber> subscribers;

	protected AbstractPublication(String name) {
		this.name = name;
		subscribers = new ArrayList<Subscriber>();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getIssue() {
		return name + " - " + issueDate;
	}

	@Override
	public void addSubscriber(Subscriber subscriber) {
		subscribers.add(subscriber);
	}

	@Override
	public void removeSubscriber(Subscriber subscriber) {
		subscribers.remove(subscriber);
	}
	
	@Override
	public void listSubscribers() {
		for (Subscriber subscriber : subscribers)
			System.out.println(subscriber.getName());
	}
}
