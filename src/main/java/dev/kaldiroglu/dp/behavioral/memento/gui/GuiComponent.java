package dev.kaldiroglu.dp.behavioral.memento.gui;

public class GuiComponent {
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

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	/** Puts the current values in a new memento, so later changes cannot reach it. */
	public GuiComponentMemento createMemento() {
		return new GuiComponentMemento(new GuiComponentState(x, y, length, width));
	}

	public void restore(GuiComponentMemento memento) {
		GuiComponentState state = memento.getState();
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
