package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.pattern;

public interface Help {
	void show();

	/** Adds help at the end of this help's list, so no help already there is replaced. */
	void addHelp(Help help);

	Help getOtherHelp();
}
