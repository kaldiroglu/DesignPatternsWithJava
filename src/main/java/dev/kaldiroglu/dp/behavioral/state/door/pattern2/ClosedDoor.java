package dev.kaldiroglu.dp.behavioral.state.door.pattern2;

/** The closed state. It knows no other state and does not change the door. */
public class ClosedDoor implements DoorState {
	@Override
	public void open() {
	}

	@Override
	public void close() {
		System.out.println("Door is already closed.");
	}

	@Override
	public boolean isOpen() {
		return false;
	}
}
