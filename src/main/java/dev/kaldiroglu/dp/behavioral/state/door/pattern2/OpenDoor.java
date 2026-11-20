package dev.kaldiroglu.dp.behavioral.state.door.pattern2;

/** The open state. It knows no other state and does not change the door. */
public class OpenDoor implements DoorState {
	@Override
	public void open() {
		System.out.println("Door is already open.");
	}

	@Override
	public void close() {
	}

	@Override
	public boolean isOpen() {
		return true;
	}
}
