package dev.kaldiroglu.dp.behavioral.memento.pattern1;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The caretaker: a thread that keeps the mementos and decides when to save and when to
 * undo. It never reads the state inside a memento.
 */
public class Caretaker extends Thread{
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

	/** Goes back to the state saved before the last one. */
	public void undo() {
		if (history.size() < 2) {
			System.err.println("Caretaker: Nothing to undo.");
			return;
		}
		history.pop();
		Memento previous = history.peek();
		originator.restore(previous);
		System.err.println("Caretaker: Undoing to: " + previous.getState());
	}

	public void run() {
		for(int i = 0; i < 11; i++) {
			if(i != 0 && i % 5 == 0)
				undo();
			else
				save();
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
