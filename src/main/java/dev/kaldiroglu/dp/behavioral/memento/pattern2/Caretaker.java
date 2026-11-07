package dev.kaldiroglu.dp.behavioral.memento.pattern2;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The caretaker: it keeps the mementos and decides when to save and when to undo. It cannot
 * read the state inside a memento, so it prints the originator instead.
 */
public class Caretaker {
	private final Originator originator;
	private final Deque<Originator.Memento> history = new ArrayDeque<>();

	public Caretaker(Originator originator) {
		this.originator = originator;
	}

	public void save() {
		history.push(originator.createMemento());
		System.out.println("Caretaker: Saved memento " + history.size() + ": " + originator);
	}

	/** Goes back to the last saved state, and forgets that save. */
	public void undo() {
		if (history.isEmpty()) {
			System.out.println("Caretaker: Nothing to undo.");
			return;
		}
		originator.restore(history.pop());
		System.out.println("Caretaker: Undid to: " + originator);
	}
}
