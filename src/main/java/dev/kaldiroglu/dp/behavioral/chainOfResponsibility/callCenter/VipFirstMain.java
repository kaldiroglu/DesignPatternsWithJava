package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

/**
 * The same desks and customers as Main, with the VIP desk first. The VIP desk serves every
 * customer, so it answers all three calls and the other desks receive none.
 */
public class VipFirstMain {

	public static void main(String[] args) {
		CallTaker first = new VipCallTaker(new GoldCallTaker(new StandardCallTaker(null)));
		for (Customer customer : Main.customers()) {
			first.answer(customer);
			System.out.println();
		}
	}
}
