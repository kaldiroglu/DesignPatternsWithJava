package dev.kaldiroglu.dp.behavioral.memento.pattern2;

/**
 * The originator, with its memento nested inside it.
 * <p>
 * The memento's field and constructor are private. Because the class is nested, the
 * originator can read the field; no other class can. The originator therefore needs no
 * public {@code getState()}.
 */
public class Originator {
	private String state;

	public Originator(String state) {
		this.state = state;
	}

	public void setState(String state) {
		System.out.println("New state: " + state);
		this.state = state;
	}

	public Memento createMemento() {
		return new Memento(state);
	}

	public void restore(Memento memento) {
		this.state = memento.state;
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
