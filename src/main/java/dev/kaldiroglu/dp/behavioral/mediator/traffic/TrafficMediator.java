package dev.kaldiroglu.dp.behavioral.mediator.traffic;

public interface TrafficMediator {
	
	public void askPermitToPass(Vehicle vehicle);

	public void done(Vehicle vehicle);
}
