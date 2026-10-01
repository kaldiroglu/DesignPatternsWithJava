package dev.kaldiroglu.dp.behavioral.command.lender.problem2;

public class ConcreteBorrower1 implements Borrower{
	
	public void borrow(int money) {
		System.out.println("Borrowing " + money + " and spending for family!");
	}
}
