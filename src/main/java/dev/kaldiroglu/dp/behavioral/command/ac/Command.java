package dev.kaldiroglu.dp.behavioral.command.ac;

public interface Command {
	
	public void execute();
	
	public void undo();
	
	public void redo();
}
