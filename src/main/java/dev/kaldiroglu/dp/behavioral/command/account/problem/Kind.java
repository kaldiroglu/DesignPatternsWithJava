package dev.kaldiroglu.dp.behavioral.command.account.problem;

/**
 * The operations a teller can perform, as names.
 * <p>
 * Stages two and three remember an operation by writing down its name and its amount, and
 * then branch on the name to reverse it. Every constant here is a case in two switches.
 */
public enum Kind {
    DEPOSIT, WITHDRAW
}
