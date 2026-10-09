package dev.kaldiroglu.dp.behavioral.memento.pattern2;

/**
 * The originator, with its memento nested inside it.
 * <p>
 * The memento's field and constructor are private. Because the class is nested, the
 * originator can read the field; no other class can. The originator therefore needs no
 * public {@code getState()}.
 */
public class Originator {
	private volatile String state;

	public Originator(String state) {
		this.state = state;
	}

	public synchronized void setState(String state) {
		System.out.println("\nNew state: " + state);
		this.state = state;
	}

	public synchronized Memento createMemento() {
		return new Memento(state);
	}

	public void restore(Memento memento) {
		setState(memento.state);
	}

	@Override
	public String toString() {
		return "Originator [state=" + state + "]";
	}

	/** The memento: the caretaker can keep it, but cannot read it. */
	public static final class Memento {
		private final String state;

		private Memento(String state) {
			this.state = state;
		}
	}
}
