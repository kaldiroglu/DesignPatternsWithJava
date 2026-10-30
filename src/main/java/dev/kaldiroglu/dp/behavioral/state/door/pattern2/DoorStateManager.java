package dev.kaldiroglu.dp.behavioral.state.door.pattern2;

public class DoorStateManager {
	private Door door;
	
	private DoorState openState;
	private DoorState closedState;
	
	public DoorStateManager(Door door) {
		this.door = door;
		openState = new OpenDoor(this);
		closedState = new ClosedDoor(this);
		door.changeState(closedState);
	}

	/** The one place that says which state follows which. */
	public void next(DoorState current) {
		door.changeState(current == openState ? closedState : openState);
	}
}
