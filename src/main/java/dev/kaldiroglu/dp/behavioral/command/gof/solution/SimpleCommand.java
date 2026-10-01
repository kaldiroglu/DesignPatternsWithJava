package dev.kaldiroglu.dp.behavioral.command.gof.solution;

import java.util.function.Consumer;

/**
 * GoF's {@code SimpleCommand}: one class for every command that only forwards.
 * <p>
 * In the book it is a C++ template holding a receiver and a pointer to one of its member
 * functions, so that a paste needs no {@code PasteCommand} class of its own. That is GoF
 * implementation issue 4 (using C++ templates). In Java the member-function pointer is a
 * method reference, and the whole of {@code PasteCommand} becomes
 * <pre>{@code new SimpleCommand<>(document, Document::paste)}</pre>
 * <p>
 * The limit is the same in both languages: it suits commands that are not undoable and
 * take no arguments. Anything that has to remember what it did needs a class.
 */
public final class SimpleCommand<R> implements Command {

    private final R receiver;
    private final Consumer<R> action;

    public SimpleCommand(R receiver, Consumer<R> action) {
        this.receiver = receiver;
        this.action = action;
    }

    @Override
    public void execute() {
        action.accept(receiver);
    }
}
