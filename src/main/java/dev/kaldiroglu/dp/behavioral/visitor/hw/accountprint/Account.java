package dev.kaldiroglu.dp.behavioral.visitor.hw.accountprint;

/**
 * Homework 1: print every account of a bank to an output that is passed in.
 * <p>
 * The accounts do not print themselves. Each printer is a visitor, and it is given the
 * output — the console, a file, a string — when it is created. A new format is a new
 * visitor; the account classes do not change.
 */
public interface Account {

    String owner();

    void accept(AccountVisitor visitor);
}
