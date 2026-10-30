package dev.kaldiroglu.dp.behavioral.mediator.traffic;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		Junction junction = new Junction("Flower");
		TrafficMediator police = new TrafficPolice("Mehmet", junction);

		System.out.println();

		int numberOfCars = 5;
		Thread[] cars = new Thread[numberOfCars];

		for (int i = 0; i < numberOfCars; i++) {
			cars[i] = new Thread(new Car("Car" + i, police));
		}

		for (Thread car : cars)
			car.start();

		for (Thread car : cars)
			car.join();
	}
}
