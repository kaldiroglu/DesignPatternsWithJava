package dev.kaldiroglu.dp.behavioral.observer.publisher;

public abstract class AbstractSubscriber implements Subscriber {
	protected String name;

	public AbstractSubscriber(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
