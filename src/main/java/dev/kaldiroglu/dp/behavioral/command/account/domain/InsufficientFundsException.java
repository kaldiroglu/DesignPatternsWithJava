package dev.kaldiroglu.dp.behavioral.command.account.domain;

/** A withdrawal larger than the balance. Nothing has changed when this is thrown. */
public final class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String owner, Money balance, Money requested) {
        super(owner + " holds " + balance + ", which is less than " + requested);
    }
}
