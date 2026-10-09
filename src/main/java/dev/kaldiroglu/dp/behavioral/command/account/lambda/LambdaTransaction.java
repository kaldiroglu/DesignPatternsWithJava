package dev.kaldiroglu.dp.behavioral.command.account.lambda;

import dev.kaldiroglu.dp.behavioral.command.account.solution.Transaction;

import java.util.function.Supplier;

/**
 * A transaction made of three functions instead of a class.
 * <p>
 * {@link Transaction} has three methods, so a single lambda cannot implement it. This record
 * holds one function for each method. The description is a {@link Supplier}, not a string,
 * because a close-out only knows what it paid after it has run.
 */
public record LambdaTransaction(Runnable onExecute, Runnable onUndo, Supplier<String> describe)
        implements Transaction {

    @Override
    public void execute() {
        onExecute.run();
    }

    @Override
    public void undo() {
        onUndo.run();
    }

    @Override
    public String description() {
        return describe.get();
    }
}
