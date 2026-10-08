package dev.kaldiroglu.dp.behavioral.state.door.pattern2;

public class OpenDoor extends AbstractDoor {

	public OpenDoor(DoorStateManager dsm) {
		super(true, dsm);
	}

	@Override
	public void open() {
		System.out.println("Door is already open.");
	}

	@Override
	public void close() {
		dsm.closeDoor();
	}
}
