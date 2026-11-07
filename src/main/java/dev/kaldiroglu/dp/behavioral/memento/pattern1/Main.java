package dev.kaldiroglu.dp.behavioral.memento.pattern1;

/**
 * The caretaker saves twice, the state changes again, and two undos go back one save at a
 * time: first to state-3, then to state-1.
 */
public class Main {

	public static void main(String[] args) {
		Originator originator = new Originator("state-0");
		Caretaker caretaker = new Caretaker(originator);

		originator.setState("state-1");
		caretaker.save();
		originator.setState("state-2");
		originator.setState("state-3");
		caretaker.save();
		originator.setState("state-4");

		caretaker.undo();
		caretaker.undo();
		caretaker.undo();
		System.out.println("Final: " + originator);
	}
}
