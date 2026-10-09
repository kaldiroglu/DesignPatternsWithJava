package dev.kaldiroglu.dp.behavioral.memento.pattern1;

/**
 * The memento: the originator's state at one moment. It only holds the state.
 * <p>
 * In this package the memento is a separate class with a public getter, so any class can
 * read the state inside it. Package {@code pattern2} nests the memento in the originator, so
 * only the originator can read it.
 */
public class Memento {
	private final String state;

	public Memento(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}
}
