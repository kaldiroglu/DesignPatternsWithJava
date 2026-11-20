package dev.kaldiroglu.dp.behavioral.state.door.pattern2;

import java.util.Map;

/**
 * The one place that decides the next state. Its table maps a state and a request to the next
 * state; a request with no line keeps the state. A new state or a new request is new lines here,
 * and the existing states do not change.
 */
public class DoorStateManager {
	private final DoorState openState = new OpenDoor();
	private final DoorState closedState = new ClosedDoor();
	private final Map<DoorState, Map<Request, DoorState>> next = Map.of(
			closedState, Map.of(Request.OPEN, openState),
			openState, Map.of(Request.CLOSE, closedState));

	public DoorState initial() {
		return closedState;
	}

	public DoorState next(DoorState current, Request request) {
		return next.get(current).getOrDefault(request, current);
	}
}
