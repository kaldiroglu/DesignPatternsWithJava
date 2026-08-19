package dev.kaldiroglu.dp.behavioral.strategy.hw.validation;

import java.util.List;

/** One condition, and the reason the exercise asks whether this deserves a class. */
public final class MinimumLength implements PassphraseRule {

    private final int minimum;

    public MinimumLength(int minimum) {
        this.minimum = minimum;
    }

    @Override
    public String name() {
        return "MIN_LENGTH_" + minimum;
    }

    @Override
    public List<String> complaints(String passphrase) {
        return passphrase.length() >= minimum
                ? List.of()
                : List.of("shorter than " + minimum + " characters");
    }
}
