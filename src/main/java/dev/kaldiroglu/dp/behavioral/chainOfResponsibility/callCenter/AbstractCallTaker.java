package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

public abstract class AbstractCallTaker implements CallTaker{
	protected CallTaker next;
	
	public AbstractCallTaker(CallTaker next) {
		this.next = next;
	}

	public CallTaker getNext() {
		return next;
	}

	/** Gives the customer to the next desk. A desk with no next desk tells the customer so. */
	protected void passOn(Customer customer) {
		if (next != null)
			next.answer(customer);
		else
			customer.receiveAnswer("No desk can answer you.");
	}
}
