package dev.kaldiroglu.dp.behavioral.observer.publisher;

public interface Publication {

	String getName();

	void addSubscriber(Subscriber subscriber);

	void removeSubscriber(Subscriber subscriber);

	void publish(String date);

	void listSubscribers();
}
