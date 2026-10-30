package dev.kaldiroglu.dp.behavioral.mediator.traffic;

import java.util.concurrent.CountDownLatch;

/**
 * A car knows only the mediator. It asks once, then blocks until the officer tells it to
 * proceed, so it does not poll.
 */
public class Car implements Vehicle, Runnable {
	private static final int PASSING_MILLIS = 100;

	private final String name;
	private final TrafficMediator mediator;
	private final CountDownLatch permit = new CountDownLatch(1);

	public Car(String name, TrafficMediator mediator) {
		this.name = name;
		this.mediator = mediator;
	}

	@Override
	public void approach() {
		System.out.println("Car " + name + " is approaching the junction.");
	}

	/** Called by the officer, often from another car's thread. It only releases this car. */
	@Override
	public void proceed() {
		permit.countDown();
	}

	@Override
	public void stop() {
		System.out.println("Car " + name + " has stopped.");
	}

	@Override
	public void run() {
		approach();
		System.out.println("Car " + name + " is asking permit to pass the junction.");
		mediator.askPermitToPass(this);
		try {
			permit.await();
			System.out.println("Car " + name + " is proceeding through the junction.");
			Thread.sleep(PASSING_MILLIS);
			mediator.done(this);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
