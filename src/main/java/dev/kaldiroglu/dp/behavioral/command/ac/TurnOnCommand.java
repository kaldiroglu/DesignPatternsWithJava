package dev.kaldiroglu.dp.behavioral.command.ac;

public class TurnOnCommand implements Command {
	private AirConditioner ac;
	private Temperature temperature;
	
	public TurnOnCommand(AirConditioner ac, Temperature temperature) {
		this.ac = ac;
		this.temperature = temperature;
	}
	
	public void execute() {
		ac.turnOn(temperature);
	}

	@Override
	public void undo() {
		// Left open: exercise set two writes it.
		
	}

	@Override
	public void redo() {
		// Left open: exercise set two writes it.
		
	}
}
