package dev.kaldiroglu.dp.behavioral.templateMethod.task;

public class Scan extends Task {

	public Scan(int interval, int repetition) {
		super(interval, repetition);
	}

	@Override
	protected void doTask() {
		System.out.println("I'm scanning!");
	}
}
