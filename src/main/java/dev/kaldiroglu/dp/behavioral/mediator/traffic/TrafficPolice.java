package dev.kaldiroglu.dp.behavioral.mediator.traffic;

import java.util.ArrayList;
import java.util.List;

/**
 * The traffic police officer: the mediator between the cars at a junction.
 * <p>
 * Every car calls this object from its own thread. The check "is the junction busy?" and
 * the step "make it busy" therefore happen together, inside one synchronized block, so two
 * cars can never both find it free. The car proceeds or waits outside the lock, so a
 * waiting car does not hold up the others.
 */
public class TrafficPolice implements TrafficMediator {
	private Junction junction;
	private List<Vehicle> vehicles;

	public TrafficPolice(String name, Junction junction) {
		this.junction = junction;
		vehicles = new ArrayList<>();
		System.out.println("TrafficPolice " + name + " created.");
	}

	@Override
	public synchronized void receive(Vehicle vehicle) {
		vehicle.stopp();
		vehicles.add(vehicle);
	}

	@Override
	public void askPermitToPass(Vehicle vehicle) {
		boolean granted;
		synchronized (this) {
			granted = !junction.isBusy();
			if (granted)
				junction.setBusy(true);
		}
		if (granted)
			vehicle.proceed();
		else
			vehicle.waitForAWhile();
	}

	@Override
	public synchronized void done(Vehicle vehicle) {
		vehicles.remove(vehicle);
		junction.setBusy(false);
	}
}
