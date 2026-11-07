package dev.kaldiroglu.dp.behavioral.state.elevator;

/** A stopped elevator can open its door. The caller decides when it starts to move. */
public class StoppedState implements ElevatorState {

	public void goDown() {
		System.out.println("Starting to go down.");
	}

	public void goUp() {
		System.out.println("Starting to go up.");
	}

	public void openDoor() {
		System.out.println("Door is open.");
	}

	public void stop() {
		System.out.println("Already stopped.");
	}
}
