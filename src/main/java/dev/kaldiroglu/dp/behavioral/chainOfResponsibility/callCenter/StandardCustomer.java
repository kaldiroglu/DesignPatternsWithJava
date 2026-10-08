package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter;

public class StandardCustomer extends AbstractCustomer{

	@Override
	public void askAQuestion() {
		askAStandardQuestion();
	}
	
	private void askAStandardQuestion() {
		System.out.println("\nStandard Customer: Asking a question!");
	}
}
