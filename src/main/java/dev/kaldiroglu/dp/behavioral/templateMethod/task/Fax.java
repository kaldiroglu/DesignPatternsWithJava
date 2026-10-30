package dev.kaldiroglu.dp.behavioral.templateMethod.task;

public class Fax extends Task {

	public Fax(int interval, int repetition) {
		super(interval, repetition);
	}

	@Override
	protected void doTask() {
		System.out.println("I'm faxing!");
	}
}
