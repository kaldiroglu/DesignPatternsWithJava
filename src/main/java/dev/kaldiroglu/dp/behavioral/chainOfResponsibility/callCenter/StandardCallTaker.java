package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

public class StandardCallTaker extends AbstractCallTaker {

	public StandardCallTaker(CallTaker next) {
		super(next);
	}

	@Override
	public void answer(Customer customer) {
		System.out.println("StandardCallTaker received a customer.");
		if (customer instanceof StandardCustomer) {
			customer.askAQuestion();
			customer.receiveAnswer("Here is your answer!");
		}
		else
			next.answer(customer);
		System.out.println();
	}
}
