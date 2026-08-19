package dev.kaldiroglu.dp.behavioral.strategy.hw.validation;

import java.util.List;

/**
 * The Strategy: what a market requires of a passphrase.
 * <p>
 * Homework 3, and the one with a trap in it. Two of these rules are a single condition each,
 * and the exercise asks whether a class per rule is worth it — the answer is not always yes.
 */
public interface PassphraseRule {

    String name();

    /** Every reason this passphrase is unacceptable. Empty means it is fine. */
    List<String> complaints(String passphrase);
}
