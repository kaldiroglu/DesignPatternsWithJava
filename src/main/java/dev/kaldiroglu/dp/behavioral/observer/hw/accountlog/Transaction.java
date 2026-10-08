package dev.kaldiroglu.dp.behavioral.observer.hw.accountlog;

/** One record of a change: who, how much, and the balance after it. */
public record Transaction(String owner, String kind, int amount, int balanceAfter) {
}
