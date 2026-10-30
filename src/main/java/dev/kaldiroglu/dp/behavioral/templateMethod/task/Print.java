package dev.kaldiroglu.dp.behavioral.templateMethod.task;

public class Print extends Task{

	public Print(int interval, int repetition) {
		super(interval, repetition);
	}
	
	protected void prepare() {
		System.out.println("*** Preparing printing! ***");
	}
	
	protected void clean() {
		System.out.println("*** Cleaning printing environment. ***");
	}

	@Override
	protected void doTask() {
		System.out.println("Printing task.");		
	}
}
