package dev.kaldiroglu.dp.behavioral.strategy.hw.latefee;

/**
 * The Strategy: what a member class is charged for keeping something too long.
 * <p>
 * Homework 2. The rules below are the ones a library actually runs; the exercise adds a
 * fourth and then asks whether it belongs here at all.
 */
public interface FeeRule {

    String name();

    /** The charge in minor units. Never negative. */
    int charge(Loan loan);
}
