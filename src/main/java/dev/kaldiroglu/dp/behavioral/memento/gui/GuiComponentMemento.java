package dev.kaldiroglu.dp.behavioral.memento.gui;

/**
 * Holds one saved state. Its {@code getState} is public, so any class can read the window's
 * state through it; {@code game.solution.Player.Checkpoint} closes that.
 */
public class GuiComponentMemento {
	private final GuiComponentState state;

	public GuiComponentMemento(GuiComponentState state) {
		this.state = state;
	}

	public GuiComponentState getState() {
		return state;
	}
}
