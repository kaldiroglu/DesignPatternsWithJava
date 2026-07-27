package dev.kaldiroglu.dp.structural.composite.gof.equipment;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The {@code Currency} value type used by the GoF equipment example (p. 170).
 *
 * <p>A tiny immutable wrapper over {@link BigDecimal} so that money is never
 * held in a binary floating-point type. It exists only to keep the pattern code
 * readable — it is not part of the pattern.</p>
 *
 * @param amount the monetary amount, always scaled to two decimal places
 */
public record Currency(BigDecimal amount) implements Comparable<Currency> {

    /** Zero money — the identity used when summing a composite's children. */
    public static final Currency ZERO = of(0);

    public Currency {
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    /** Creates an amount from a plain number, e.g. {@code Currency.of(24.50)}. */
    public static Currency of(double value) {
        return new Currency(BigDecimal.valueOf(value));
    }

    /** Returns the sum of this amount and {@code other}. */
    public Currency plus(Currency other) {
        return new Currency(amount.add(other.amount));
    }

    /** Returns this amount multiplied by {@code factor}, e.g. a discount rate. */
    public Currency times(double factor) {
        return new Currency(amount.multiply(BigDecimal.valueOf(factor)));
    }

    @Override
    public int compareTo(Currency other) {
        return amount.compareTo(other.amount);
    }

    @Override
    public String toString() {
        return "$" + amount.toPlainString();
    }
}
