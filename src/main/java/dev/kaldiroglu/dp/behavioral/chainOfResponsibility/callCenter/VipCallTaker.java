package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

public class VipCallTaker extends AbstractCallTaker {

	public VipCallTaker(CallTaker next) {
		super(next);
	}

	@Override
	public void answer(Customer customer) {
		System.out.println("VipCallTaker received a customer.");
		if (customer instanceof VipCustomer) {
			customer.askAQuestion();
			customer.receiveAnswer("Here is your VIP answer!");
		}
		else
			passOn(customer);
		System.out.println();
	}
}
