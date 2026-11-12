package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

import java.util.List;

/**
 * Builds the chain standard, then gold, then VIP, and sends one customer of each kind to
 * the standard desk. Each customer is answered by the first desk that serves them.
 */
public class Main {

	public static void main(String[] args) {
		CallTaker first = createCallTakers();
		for (Customer customer : customers()) {
			first.answer(customer);
			System.out.println();
		}
	}

	static CallTaker createCallTakers() {
		VipCallTaker vipCT = new VipCallTaker(null);
		GoldCallTaker goldCT = new GoldCallTaker(vipCT);
		StandardCallTaker standardCT = new StandardCallTaker(goldCT);
		return standardCT;
	}

	static List<Customer> customers() {
		return List.of(new StandardCustomer(), new GoldCustomer(), new VipCustomer());
	}
}
