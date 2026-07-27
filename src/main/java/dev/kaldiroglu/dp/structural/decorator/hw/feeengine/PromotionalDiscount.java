package dev.kaldiroglu.dp.structural.decorator.hw.feeengine;

import java.math.BigDecimal;

/** Takes a percentage off. */
public final class PromotionalDiscount extends Adjustment {

    private final BigDecimal rate;

    public PromotionalDiscount(Charge component, String percent) {
        super(component);
        this.rate = new BigDecimal(percent).movePointLeft(2);
    }

    @Override
    protected BigDecimal adjust(BigDecimal base) {
        return base.subtract(base.multiply(rate));
    }
}
