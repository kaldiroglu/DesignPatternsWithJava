package dev.kaldiroglu.dp.behavioral.state.door.pattern1;

/** The <b>State</b>: only the requests a door takes. The wiring is not part of it. */
public interface DoorState {
	void open();

	void close();

	boolean isOpen();
}
