package dev.kaldiroglu.dp.structural.decorator.hw.feeengine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * The Decorator: one adjustment to an amount.
 * <p>
 * Money is {@link BigDecimal} and every subclass rounds to two places, half up, because the
 * alternative is a fee engine that disagrees with the customer's bank by a kuruş and cannot
 * say why.
 */
public abstract class Adjustment implements Charge {

    protected final Charge component;

    protected Adjustment(Charge component) {
        this.component = Objects.requireNonNull(component);
    }

    protected static BigDecimal money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    /** What this adjustment does to the amount underneath it. */
    protected abstract BigDecimal adjust(BigDecimal base);

    @Override
    public final BigDecimal amount() {
        return money(adjust(component.amount()));
    }
}
