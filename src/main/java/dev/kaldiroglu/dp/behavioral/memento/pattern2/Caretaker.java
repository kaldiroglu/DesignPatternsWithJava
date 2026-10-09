package dev.kaldiroglu.dp.behavioral.memento.pattern2;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The caretaker: a thread that keeps the mementos and decides when to save and when to
 * undo. It cannot read the state inside a memento, so it reports only how many it keeps.
 */
public class Caretaker extends Thread{
	private final Originator originator;
	private final Deque<Originator.Memento> history = new ArrayDeque<>();

	public Caretaker(Originator originator) {
		this.originator = originator;
	}

	public void save() {
		history.push(originator.createMemento());
		System.out.println("Caretaker: Saved memento " + history.size() + ": " + originator);
	}

	/** Goes back to the state saved before the last one. */
	public void undo() {
		if (history.size() < 2) {
			System.err.println("Caretaker: Nothing to undo.");
			return;
		}
		history.pop();
		originator.restore(history.peek());
		System.err.println("Caretaker: Undid to memento " + history.size() + ": " + originator);
	}

	public void run() {
		for(int i = 0; i < 10; i++) {
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
