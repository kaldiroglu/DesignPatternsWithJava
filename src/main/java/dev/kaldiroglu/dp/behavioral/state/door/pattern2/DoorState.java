package dev.kaldiroglu.dp.behavioral.state.door.pattern2;

/** The <b>State</b>: it does the work of a request. It does not choose the next state. */
public interface DoorState {
	void open();

	void close();

	boolean isOpen();
}
