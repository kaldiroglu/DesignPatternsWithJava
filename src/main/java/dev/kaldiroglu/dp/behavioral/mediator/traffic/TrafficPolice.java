package dev.kaldiroglu.dp.behavioral.mediator.traffic;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * The traffic police officer: the mediator between the cars at a junction.
 * <p>
 * A car asks once. If the junction is free, the officer tells it to proceed. If not, the
 * officer tells it to stop and keeps it in {@code waiting}. When a car is done, the officer
 * tells the first waiting car to proceed, so no car has to ask again, and the junction stays
 * busy while it passes from one car to the next.
 * <p>
 * Every car calls this object from its own thread, so the decisions are made inside one
 * synchronized block. The officer calls the car outside the lock, so a car does not hold up
 * the others.
 */
public class TrafficPolice implements TrafficMediator {
	private final Junction junction;
	private final Queue<Vehicle> waiting;

	public TrafficPolice(String name, Junction junction) {
		this.junction = junction;
		waiting = new ArrayDeque<>();
		System.out.println("TrafficPolice " + name + " created.");
	}

	@Override
	public void askPermitToPass(Vehicle vehicle) {
		boolean granted;
		synchronized (this) {
			granted = !junction.isBusy();
			if (granted)
				junction.setBusy(true);
			else
				waiting.add(vehicle);
		}
		if (granted)
			vehicle.proceed();
		else
			vehicle.stop();
	}

	@Override
	public void done(Vehicle vehicle) {
		Vehicle next;
		synchronized (this) {
			next = waiting.poll();
			if (next == null)
				junction.setBusy(false);
		}
		if (next != null)
			next.proceed();
	}
}
