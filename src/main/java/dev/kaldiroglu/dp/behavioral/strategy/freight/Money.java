package dev.kaldiroglu.dp.behavioral.strategy.freight;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** An amount, held to two places so no example ever argues about rounding. */
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

    /** Multiplies by a count of units, rounding half up at two places. */
    public Money times(double units) {
        return new Money(amount.multiply(BigDecimal.valueOf(units)));
    }

    public Money percentMore(int percent) {
        return new Money(amount.multiply(BigDecimal.valueOf(100 + percent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
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
