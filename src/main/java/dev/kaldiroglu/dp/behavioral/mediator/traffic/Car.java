package dev.kaldiroglu.dp.behavioral.mediator.traffic;

public class Car extends Thread implements Vehicle {
	private Junction junction;
	private TrafficMediator mediator;
	private boolean waiting;

	public Car(String name, Junction junction, TrafficMediator mediator) {
		super(name);
		this.junction = junction;
		this.mediator = mediator;
		approach();
		mediator.receive(this);
	}
	
	@Override
	public void approach() {
		System.out.println("Car " + getName() + " is approaching junction " + junction.getName());
	}

	@Override
	public void proceed() {
		System.out.println("Car " + getName() + " is proceeding through junction " + junction.getName());
		mediator.done(this);
	}

	@Override
	public void stopp() {
		System.out.println("Car " + getName() + " has stopped.");
	}

	/** Waits, then lets run() ask again. It does not call the mediator itself. */
	@Override
	public void waitForAWhile() {
		System.out.println("Car " + getName() + " is waiting.");
		waiting = true;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	@Override
	public void run() {
		System.out.println("Car " + getName() + " is asking permit to pass junction " + junction.getName());
		do {
			waiting = false;
			mediator.askPermitToPass(this);
		} while (waiting);
	}
}
