package dev.kaldiroglu.dp.behavioral.templateMethod.task;

public class Fax extends Task {

	public Fax(String name, int interval, int repetation) {
		super(name, interval, repetation);
	}

	@Override
	public void doTask() {
		System.out.println("I'm faxing!");
	}
}
