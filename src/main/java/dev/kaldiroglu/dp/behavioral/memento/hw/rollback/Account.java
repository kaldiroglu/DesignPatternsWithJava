package dev.kaldiroglu.dp.behavioral.memento.hw.rollback;

/** Homework 2: the <b>Originator</b>. An account that cannot go below zero. */
public final class Account {

    /** The <b>Memento</b>: the balance at one moment. */
    public static final class Saved {
        private final int balance;

        private Saved(int balance) {
            this.balance = balance;
        }
    }

    private final String owner;
    private int balance;

    public Account(String owner, int balance) {
        this.owner = owner;
        this.balance = balance;
    }

    public void withdraw(int amount) {
        if (amount > balance) {
            throw new IllegalStateException(owner + " cannot pay " + amount);
        }
        balance -= amount;
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public Saved save() {
        return new Saved(balance);
    }

    public void restore(Saved saved) {
        balance = saved.balance;
    }

    @Override
    public String toString() {
        return owner + " " + balance;
    }
}
