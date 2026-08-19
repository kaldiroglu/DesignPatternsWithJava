package dev.kaldiroglu.dp.behavioral.strategy.pricing.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * An amount in minor-unit-safe decimal, so the examples never argue about rounding.
 * <p>
 * Every amount is held to two places and rounded half-up, which is what a till does. The
 * type exists so a pricing rule can be read as arithmetic about money rather than as
 * arithmetic about {@code double}.
 */
public record Money(BigDecimal amount) implements Comparable<Money> {

    public static final Money ZERO = of("0.00");

    public Money {
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(String amount) {
        return new Money(new BigDecimal(amount));
    }

    public Money plus(Money other) {
        return new Money(amount.add(other.amount));
    }

    public Money minus(Money other) {
        return new Money(amount.subtract(other.amount));
    }

    public Money times(int count) {
        return new Money(amount.multiply(BigDecimal.valueOf(count)));
    }

    /** Takes a percentage off, so {@code percentOff(20)} leaves 80% of the amount. */
    public Money percentOff(int percent) {
        BigDecimal kept = BigDecimal.valueOf(100 - percent)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return new Money(amount.multiply(kept));
    }

    public boolean isAtLeast(Money other) {
        return amount.compareTo(other.amount) >= 0;
    }

    @Override
    public int compareTo(Money other) {
        return amount.compareTo(other.amount);
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }
}
