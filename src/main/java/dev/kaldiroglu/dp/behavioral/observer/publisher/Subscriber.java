package dev.kaldiroglu.dp.behavioral.observer.publisher;

public interface Subscriber {

	public String getName();

	public void receive(Publication publication);
}
