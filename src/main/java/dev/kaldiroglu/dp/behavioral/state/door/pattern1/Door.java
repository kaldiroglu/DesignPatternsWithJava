package dev.kaldiroglu.dp.behavioral.state.door.pattern1;

/**
 * The <b>Context</b>. It forwards each request to its state, and the state changes it.
 * Every door makes its own two state objects, because each state holds its door.
 */
public class Door {
	private final AbstractDoorState openState = new OpenDoor();
	private final AbstractDoorState closedState = new ClosedDoor();
	private DoorState state = closedState;

	public Door() {
		openState.wire(this, openState, closedState);
		closedState.wire(this, openState, closedState);
	}

	public void open() {
		state.open();
	}

	public void close() {
		state.close();
	}

	public boolean isOpen() {
		return state.isOpen();
	}

	void changeState(DoorState state) {
		this.state = state;
	}
}
