package dev.kaldiroglu.dp.behavioral.memento.gui;

/** The caretaker: it keeps the memento and never looks inside it. */
public class Main {

	public static void main(String[] args) {
		GuiComponent window = new GuiComponent("window", 0, 0, 20, 10);
		System.out.println(window);

		GuiComponentMemento saved = window.createMemento();

		window.setX(20);
		window.setLength(40);
		System.out.println(window);

		window.restore(saved);
		System.out.println(window);
	}
}
