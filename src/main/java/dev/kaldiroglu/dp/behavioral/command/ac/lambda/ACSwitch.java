package dev.kaldiroglu.dp.behavioral.command.ac.lambda;

import dev.kaldiroglu.dp.behavioral.command.ac.AirConditioner;
import dev.kaldiroglu.dp.behavioral.command.ac.Temperature;

import java.util.function.Consumer;

/**
 * The wall switch of {@code ac}, with each request as a function instead of a command class.
 * <p>
 * {@code ac.Command} has three methods — {@code execute}, {@code undo}, {@code redo} — so a
 * lambda cannot implement it. Here each request is a method reference to the air
 * conditioner: a {@link Consumer} of a temperature, or a {@link Runnable} for turning off.
 * The four command classes are gone. So are {@code undo} and {@code redo}: in {@code ac}
 * they are empty, and a function cannot remember what it did. A request that must be
 * undone needs a class, as the teller's transactions show.
 */
public class ACSwitch {
	private final Consumer<Temperature> turnOn;
	private final Runnable turnOff;
	private final Consumer<Temperature> turnOnHeater;
	private final Consumer<Temperature> turnOnCooler;

	public ACSwitch() {
		AirConditioner ac = new AirConditioner(new Temperature(22));
		turnOn = ac::turnOn;
		turnOff = ac::turnOff;
		turnOnHeater = ac::turnOnHeater;
		turnOnCooler = ac::turnOnCooler;
	}

	public void turnOn(int temperature) {
		turnOn.accept(new Temperature(temperature));
	}

	public void turnOff() {
		turnOff.run();
	}

	public void turnOnHeater(int temperature) {
		turnOnHeater.accept(new Temperature(temperature));
	}

	public void turnOnCooler(int temperature) {
		turnOnCooler.accept(new Temperature(temperature));
	}
}
