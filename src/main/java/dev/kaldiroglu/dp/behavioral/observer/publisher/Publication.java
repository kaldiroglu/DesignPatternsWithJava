package dev.kaldiroglu.dp.behavioral.observer.publisher;

public interface Publication {

	String getName();

	/** The magazine's name and the date of its latest issue. */
	String getIssue();

	void addSubscriber(Subscriber subscriber);

	void removeSubscriber(Subscriber subscriber);

	void publish(String date);

	void listSubscribers();
}
