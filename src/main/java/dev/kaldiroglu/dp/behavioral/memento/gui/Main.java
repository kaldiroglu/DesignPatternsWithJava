package dev.kaldiroglu.dp.behavioral.memento.gui;

/** The caretaker: it keeps the memento and never looks inside it. */
public class Main {

	public static void main(String[] args) {
		GuiComponent window = new GuiComponent("window", 0, 0, 20, 10);
		System.out.println(window);

		GuiComponent.Memento saved = window.createMemento();

		window.moveTo(20, 0);
		window.resize(40, 10);
		System.out.println(window);

		window.restore(saved);
		System.out.println(window);
	}
}
