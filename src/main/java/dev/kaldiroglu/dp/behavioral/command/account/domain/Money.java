package dev.kaldiroglu.dp.behavioral.command.account.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * An amount of lira, always to two decimal places.
 * <p>
 * A value, not an entity: two {@code Money} objects holding the same amount are equal, and
 * nothing ever changes one in place. Every operation answers a new value.
 */
public record Money(BigDecimal amount) implements Comparable<Money> {

    public static final Money ZERO = Money.of("0.00");

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

    public boolean isLessThan(Money other) {
        return compareTo(other) < 0;
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
