package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

public class StandardCustomer extends AbstractCustomer {

	@Override
	public void askAQuestion() {
		System.out.println("Standard customer: asking a question.");
	}
}
