package dev.kaldiroglu.dp.behavioral.state.door.pattern1;

/**
 * What both states share: the door they change, and the two states they can move it to.
 * {@link #wire} is package-private, so only {@link Door} can connect them.
 */
public abstract class AbstractDoorState implements DoorState {
	protected Door door;
	protected DoorState openState;
	protected DoorState closedState;

	void wire(Door door, DoorState openState, DoorState closedState) {
		this.door = door;
		this.openState = openState;
		this.closedState = closedState;
	}
}
