package dev.kaldiroglu.dp.behavioral.command.ac;

public class TurnOffCommand implements Command {
	private AirConditioner ac;
	
	public TurnOffCommand(AirConditioner ac) {
		this.ac = ac;
	}
	
	public void execute() {
		ac.turnOff();
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
