package dev.kaldiroglu.dp.behavioral.templateMethod.task;

public class Scan extends Task {

	public Scan(String name, int interval, int repetition) {
		super(name, interval, repetition);
	}

	@Override
	public void doTask() {
		System.out.println("I'm scanning!");
	}
}
