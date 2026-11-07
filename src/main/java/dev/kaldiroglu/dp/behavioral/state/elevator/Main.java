package dev.kaldiroglu.dp.behavioral.state.elevator;

/**
 * One trip up and one trip down. After each request, the caller sets the next state: the
 * states only print what the elevator does.
 */
public class Main {

	public static void main(String[] args) {
		Elevator elevator = new Elevator(new StoppedState());
		elevator.openDoor();

		elevator.goUp();
		elevator.setState(new GoingUpState());
		elevator.openDoor();
		elevator.goDown();

		elevator.stop();
		elevator.setState(new StoppedState());
		elevator.openDoor();

		elevator.goDown();
		elevator.setState(new GoingDownState());
		elevator.goDown();
		elevator.stop();
		elevator.setState(new StoppedState());
	}
}
