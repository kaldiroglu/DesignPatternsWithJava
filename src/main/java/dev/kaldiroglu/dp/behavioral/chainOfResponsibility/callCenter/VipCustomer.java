package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

public class VipCustomer extends AbstractCustomer {

	@Override
	public void askAQuestion() {
		System.out.println("VIP customer: asking a VIP question.");
	}
}
