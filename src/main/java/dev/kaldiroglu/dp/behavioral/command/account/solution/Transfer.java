package dev.kaldiroglu.dp.behavioral.command.account.solution;

import dev.kaldiroglu.dp.behavioral.command.account.domain.Account;
import dev.kaldiroglu.dp.behavioral.command.account.domain.Money;

import java.util.ArrayList;
import java.util.List;

/**
 * A <b>ConcreteCommand</b> made of other commands: GoF's {@code MacroCommand}, with a bank's
 * name.
 * <p>
 * A transfer still reuses the withdrawal and the deposit — the reuse was never the mistake
 * in {@code problem.Teller}. The mistake was that the teller recorded the two parts and
 * forgot the whole. Here the whole is one object, so it goes on the history once and comes
 * off it once: undo takes back both halves, in reverse order.
 * <p>
 * It is also all or nothing on the way in. If a later step fails, the steps already taken
 * are undone before the failure is passed on, so a half-made transfer is never left behind.
 */
public final class Transfer implements Transaction {

    private final List<Transaction> steps;
    private final String description;

    public Transfer(Account from, Account to, Money amount) {
        this.steps = List.of(new Withdraw(from, amount), new Deposit(to, amount));
        this.description = "transfer " + amount + " " + from.owner() + " -> " + to.owner();
    }

    @Override
    public void execute() {
        List<Transaction> taken = new ArrayList<>();
        try {
            for (Transaction step : steps) {
                step.execute();
                taken.add(step);
            }
        } catch (RuntimeException failure) {
            for (Transaction step : taken.reversed()) {
                step.undo();
            }
            throw failure;
        }
    }

    @Override
    public void undo() {
        for (Transaction step : steps.reversed()) {
            step.undo();
        }
    }

    @Override
    public String description() {
        return description;
    }
}
