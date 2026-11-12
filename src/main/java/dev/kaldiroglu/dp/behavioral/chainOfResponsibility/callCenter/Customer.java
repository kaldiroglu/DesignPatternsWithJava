package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

public interface Customer {

	void askAQuestion();

	void receiveAnswer(String answer);
}
