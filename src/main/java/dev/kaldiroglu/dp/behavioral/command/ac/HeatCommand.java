package dev.kaldiroglu.dp.behavioral.command.ac;

public class HeatCommand implements Command {
	private AirConditioner ac;
	private Temperature temperature;
	
	public HeatCommand(AirConditioner ac, Temperature temperature) {
		this.ac = ac;
		this.temperature = temperature;
	}
	
	public void execute() {
		ac.turnOnHeater(temperature);
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
