package dev.kaldiroglu.dp.behavioral.state.elevator;

/**
 * The elevator forwards each request to its current state.
 * <p>
 * The states never change the elevator's state. The caller does, with {@link #setState}.
 * That makes this example closer to Strategy than to State, which the State deck uses to
 * show the difference between the two.
 */
public class Elevator {
	private ElevatorState state;

	public Elevator(ElevatorState state) {
		this.state = state;
	}

	public void setState(ElevatorState state) {
		this.state = state;
	}

	public ElevatorState getState() {
		return state;
	}

	public void goDown() {
		state.goDown();
	}

	public void goUp() {
		state.goUp();
	}

	public void openDoor() {
		state.openDoor();
	}

	public void stop() {
		state.stop();
	}
}
