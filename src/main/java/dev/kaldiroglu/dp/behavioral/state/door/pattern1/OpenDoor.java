package dev.kaldiroglu.dp.behavioral.state.door.pattern1;

/** The open state. It knows the closed state, and moves the door there itself. */
public class OpenDoor extends AbstractDoorState {
	@Override
	public void open() {
		System.out.println("Door is already open.");
	}

	@Override
	public void close() {
		door.changeState(closedState);
	}

	@Override
	public boolean isOpen() {
		return true;
	}
}
