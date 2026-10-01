package dev.kaldiroglu.dp.behavioral.command.account.solution;

/**
 * The <b>Command</b>: one request to the bank, as an object.
 * <p>
 * GoF list "Transaction" as one of this pattern's other names (p. 233), and in a bank it is
 * the obvious one. A transaction knows which account it touches and by how much, so
 * {@link #execute()} takes no arguments: everything the request needs was given to it when
 * it was made. That is what lets a teller hold one, put it on a list, run it tonight, or
 * take it back.
 * <p>
 * Redo is not on the interface. To redo a transaction is to execute it again, so the
 * invoker does it with the method that is already here.
 */
public interface Transaction {

    void execute();

    void undo();

    /** One line for the journal. */
    String description();
}
