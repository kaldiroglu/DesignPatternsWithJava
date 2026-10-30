package dev.kaldiroglu.dp.behavioral.command.ac;

/** The invoker. Each press makes one complete request, and the switch only runs it. */
public class ACSwitch {
	private AirConditioner ac = new AirConditioner(new Temperature(22));

	public void turnOn(int temperature) {
		press(new TurnOnCommand(ac, new Temperature(temperature)));
	}

	public void turnOff() {
		press(new TurnOffCommand(ac));
	}

	public void turnOnHeater(int temperature) {
		press(new HeatCommand(ac, new Temperature(temperature)));
	}

	public void turnOnCooler(int temperature) {
		press(new CoolCommand(ac, new Temperature(temperature)));
	}

	private void press(Command command) {
		command.execute();
	}
}
