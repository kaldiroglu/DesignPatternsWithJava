package dev.kaldiroglu.dp.behavioral.command.ac;

public class CoolCommand implements Command {
	private AirConditioner ac;
	private Temperature temperature;
	
	public CoolCommand(AirConditioner ac, Temperature temperature) {
		this.ac = ac;
		this.temperature = temperature;
	}
	
	public void execute() {
		ac.turnOnCooler(temperature);
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
