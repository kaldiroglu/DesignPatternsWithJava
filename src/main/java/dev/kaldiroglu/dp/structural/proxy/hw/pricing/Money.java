package dev.kaldiroglu.dp.structural.proxy.hw.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** A price, to two places. */
public record Money(BigDecimal amount) {

    public static Money of(String amount) {
        return new Money(new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP));
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }
}
