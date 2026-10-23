package dev.kaldiroglu.dp.behavioral.command.account.problem;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

/** Stage two: the account keeps a history, so both steps can be undone. */
public final class StageTwoMain {

    public static void main(String[] args) {
        HistoryAccount two = new HistoryAccount("Deniz", Money.of("1000.00"));
        two.deposit(Money.of("500.00"));
        two.withdraw(Money.of("200.00"));
        two.undo();
        two.undo();
        System.out.println("Stage two: the same steps. Balance: " + two.balance());
        System.out.println("Stage two journal: " + two.journal());
    }
}
