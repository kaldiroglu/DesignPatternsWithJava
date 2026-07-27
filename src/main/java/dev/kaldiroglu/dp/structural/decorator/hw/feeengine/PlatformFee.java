package dev.kaldiroglu.dp.structural.decorator.hw.feeengine;

import java.math.BigDecimal;

/** Adds the marketplace's percentage cut. */
public final class PlatformFee extends Adjustment {

    private final BigDecimal rate;

    public PlatformFee(Charge component, String percent) {
        super(component);
        this.rate = new BigDecimal(percent).movePointLeft(2);
    }

    @Override
    protected BigDecimal adjust(BigDecimal base) {
        return base.add(base.multiply(rate));
    }
}
