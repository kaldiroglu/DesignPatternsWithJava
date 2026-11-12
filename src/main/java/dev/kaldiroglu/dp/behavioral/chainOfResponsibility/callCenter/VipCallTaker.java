package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

/** Serves VIP customers, and gold and standard customers too. */
public class VipCallTaker extends AbstractCallTaker {

	public VipCallTaker(CallTaker next) {
		super(next);
	}

	@Override
	public void answer(Customer customer) {
		System.out.println("VipCallTaker received a customer.");
		if (customer instanceof VipCustomer
				|| customer instanceof GoldCustomer
				|| customer instanceof StandardCustomer) {
			customer.askAQuestion();
			customer.receiveAnswer("Here is your VIP answer!");
		}
		else
			passOn(customer);
	}
}
