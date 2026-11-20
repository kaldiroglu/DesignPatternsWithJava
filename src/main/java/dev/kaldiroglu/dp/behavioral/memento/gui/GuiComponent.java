package dev.kaldiroglu.dp.behavioral.memento.gui;

/**
 * The <b>Originator</b>: a window with a position and a size.
 * <p>
 * Its state is one object, {@link GuiComponentState}. {@link #createMemento()} puts the current
 * state in a {@link Memento}, and {@link #restore} reads it back. Only this class can do either.
 */
public class GuiComponent {

	/**
	 * The <b>Memento</b>. Its field and its constructor are private, and it has no methods: the
	 * caretaker can keep it and pass it back, but cannot read it, change it or make one. Because
	 * it is nested in {@code GuiComponent}, the window can read its private field.
	 */
	public static final class Memento {
		private final GuiComponentState state;

		private Memento(GuiComponentState state) {
			this.state = state;
		}
	}

	private String name;
	private int x;
	private int y;
	private int length;
	private int width;
	
	public GuiComponent(String name, int x, int y, int length, int width) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.length = length;
		this.width = width;
	}
	
	public String getName() {
		return name;
	}

	/** Moves the window. The only way to change its position. */
	public void moveTo(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/** Changes the window's size. The only way to change its length and width. */
	public void resize(int length, int width) {
		this.length = length;
		this.width = width;
	}

	/** Puts the current values in a new memento, so later changes cannot reach it. */
	public Memento createMemento() {
		return new Memento(new GuiComponentState(x, y, length, width));
	}

	public void restore(Memento memento) {
		GuiComponentState state = memento.state;
		x = state.x();
		y = state.y();
		length = state.length();
		width = state.width();
	}

	@Override
	public String toString() {
		return "GuiComponent [name=" + name + ", x=" + x + ", y=" + y
				+ ", length=" + length + ", width=" + width + "]";
	}
}
