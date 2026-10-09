package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.pattern;

public abstract class AbstractHelp implements Help {
	protected String description;
	protected Help otherHelp;

	public String getDescription() {
		return description;
	}

	@Override
	public void addHelp(Help help) {
		if (otherHelp == null)
			otherHelp = help;
		else
			otherHelp.addHelp(help);
	}

	@Override
	public Help getOtherHelp() {
		return otherHelp;
	}

	@Override
	public void show() {
		System.out.println(description);
		if (otherHelp != null)
			otherHelp.show();
	}
}
