package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.pattern;

public interface Help {
	void show();

	void setOtherHelp(Help otherHelp);
	
	Help getOtherHelp();
}
