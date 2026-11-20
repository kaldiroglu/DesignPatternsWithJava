package dev.kaldiroglu.dp.behavioral.state.door.pattern2;

/**
 * The <b>Context</b>. It passes each request to its state, which does the work, and then asks
 * the manager for the next state.
 */
public class Door {
	private final DoorStateManager manager = new DoorStateManager();
	private DoorState state = manager.initial();

	public void open() {
		state.open();
		state = manager.next(state, Request.OPEN);
	}

	public void close() {
		state.close();
		state = manager.next(state, Request.CLOSE);
	}

	public boolean isOpen() {
		return state.isOpen();
	}
}
