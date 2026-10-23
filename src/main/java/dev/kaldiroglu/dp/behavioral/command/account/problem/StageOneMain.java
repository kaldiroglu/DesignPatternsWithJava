package dev.kaldiroglu.dp.behavioral.command.account.problem;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

/** Stage one: the account remembers only its last move, so a second undo does nothing. */
public final class StageOneMain {

    public static void main(String[] args) {
        OneStepAccount one = new OneStepAccount("Deniz", Money.of("1000.00"));
        one.deposit(Money.of("500.00"));
        one.withdraw(Money.of("200.00"));
        one.undo();
        one.undo();
        System.out.println("Stage one: deposit 500, withdraw 200, undo twice. Balance: "
                + one.balance() + " (the second undo did nothing)");
    }
}
