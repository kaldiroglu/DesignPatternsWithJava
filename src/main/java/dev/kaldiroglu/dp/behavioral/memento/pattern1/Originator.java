package dev.kaldiroglu.dp.behavioral.memento.pattern1;

/** The originator: it creates a memento of its state, and restores itself from one. */
public class Originator {
	private String state;

	public Originator(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		System.out.println("New state: " + state);
		this.state = state;
	}

	public Memento createMemento() {
		return new Memento(state);
	}

	public void restore(Memento memento) {
		this.state = memento.getState();
	}

	@Override
	public String toString() {
		return "Originator [state=" + state + "]";
	}
}
