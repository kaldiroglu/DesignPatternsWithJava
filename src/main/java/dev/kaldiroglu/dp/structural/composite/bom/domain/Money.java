package dev.kaldiroglu.dp.structural.composite.bom.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * An amount of money, held in {@link BigDecimal} so that costs never drift the
 * way binary floating-point values do.
 *
 * <p>A supporting value type, not part of the Composite solution.</p>
 *
 * @param amount the amount, always scaled to two decimal places
 */
public record Money(BigDecimal amount) implements Comparable<Money> {

    /** The identity for summation — the cost of an assembly with no children. */
    public static final Money ZERO = of(0);

    public Money {
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    /** Creates an amount from a plain number, e.g. {@code Money.of(24.50)}. */
    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    /** Returns the sum of this amount and {@code other}. */
    public Money plus(Money other) {
        return new Money(amount.add(other.amount));
    }

    /** Returns this amount repeated {@code quantity} times. */
    public Money times(int quantity) {
        return new Money(amount.multiply(BigDecimal.valueOf(quantity)));
    }

    @Override
    public int compareTo(Money other) {
        return amount.compareTo(other.amount);
    }

    @Override
    public String toString() {
        return "$" + amount.toPlainString();
    }
}
