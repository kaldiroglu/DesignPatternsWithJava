package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

public class GoldCallTaker extends AbstractCallTaker {
	
	public GoldCallTaker(CallTaker next) {
		super(next);
	}

	@Override
	public void answer(Customer customer) {
		System.out.println("GoldCallTaker received a customer.");
		if (customer instanceof GoldCustomer) {
			customer.askAQuestion();
			customer.receiveAnswer("Here is your GOLD answer!");
		}
		else
			passOn(customer);
		System.out.println();
	}
}
