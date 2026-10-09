package dev.kaldiroglu.dp.behavioral.command.lender.lambda;

import java.util.function.IntConsumer;

/**
 * The lender of {@code lender.pattern}, with the command as a function.
 * <p>
 * {@code pattern.Command} has one method, {@code execute(int)}, so it is a function from an
 * amount to nothing. The JDK already has that type: {@link IntConsumer}. The lender takes
 * one, and a lambda or a method reference can be the borrower or the tax office. No
 * {@code Command} interface and no command classes are needed.
 */
public class Lender {

	public void lend(IntConsumer command, int money) {
		command.accept(money);
	}
}
