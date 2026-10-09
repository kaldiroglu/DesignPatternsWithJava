package dev.kaldiroglu.dp.behavioral.command.ac.lambda;

/** The same steps as {@code ac.Person}, through the switch whose requests are functions. */
public class Main {

	public static void main(String[] args) {
		ACSwitch acSwitch = new ACSwitch();
		int temperature = 20;
		acSwitch.turnOn(temperature);
		acSwitch.turnOff();
		acSwitch.turnOnCooler(18);
		acSwitch.turnOnHeater(25);
		acSwitch.turnOn(temperature);
		acSwitch.turnOnCooler(18);
		acSwitch.turnOnCooler(25);
		acSwitch.turnOnCooler(15);
		acSwitch.turnOnHeater(23);
		acSwitch.turnOnHeater(20);
		acSwitch.turnOnHeater(25);
		acSwitch.turnOff();
	}
}
