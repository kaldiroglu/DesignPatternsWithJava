package dev.kaldiroglu.dp.behavioral.memento.pattern1;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The caretaker: it keeps the mementos and decides when to save and when to undo. It reads
 * the state inside a memento only to print it, which this package allows because the
 * memento has a public getter.
 */
public class Caretaker {
	private final Originator originator;
	private final Deque<Memento> history = new ArrayDeque<>();

	public Caretaker(Originator originator) {
		this.originator = originator;
	}

	public void save() {
		Memento memento = originator.createMemento();
		history.push(memento);
		System.out.println("Caretaker: Saving state: " + memento.getState());
	}

	/** Goes back to the last saved state, and forgets that save. */
	public void undo() {
		if (history.isEmpty()) {
			System.out.println("Caretaker: Nothing to undo.");
			return;
		}
		Memento last = history.pop();
		originator.restore(last);
		System.out.println("Caretaker: Undoing to: " + last.getState());
	}
}
