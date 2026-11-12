package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

public class GoldCustomer extends AbstractCustomer {

	@Override
	public void askAQuestion() {
		System.out.println("Gold customer: asking a GOLD question.");
	}
}
