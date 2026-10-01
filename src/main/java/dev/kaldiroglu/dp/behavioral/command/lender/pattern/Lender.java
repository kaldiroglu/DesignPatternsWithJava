package dev.kaldiroglu.dp.behavioral.command.lender.pattern;

public class Lender {
	public void lend(Command command, int money) {
		command.execute(money);
	}
}
