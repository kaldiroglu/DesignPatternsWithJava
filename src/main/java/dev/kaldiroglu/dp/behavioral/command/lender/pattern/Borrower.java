package dev.kaldiroglu.dp.behavioral.command.lender.pattern;

public class Borrower implements Command {

	@Override
	public void execute(int money) {
		System.out.println("Borrowing " + money + " and spending for family!");
	}
}
