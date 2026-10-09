package dev.kaldiroglu.dp.behavioral.command.lender.lambda;

import java.util.function.IntConsumer;

/** The same two loans as {@code lender.pattern.Main}, with lambdas instead of command classes. */
public class Main {

	public static void main(String[] args) {
		IntConsumer borrower = money ->
				System.out.println("Borrowing " + money + " and spending for family!");
		IntConsumer taxOffice = money -> System.out.println("Receiving for the tax payment!");

		Lender lender = new Lender();
		lender.lend(borrower, 1000);
		lender.lend(taxOffice, 2000);
	}
}
