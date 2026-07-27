package dev.kaldiroglu.dp.structural.decorator.hw.feeengine;

import java.math.BigDecimal;

/**
 * Adds VAT.
 * <p>
 * <strong>Where this sits is not a matter of taste.</strong> A promotional discount reduces
 * the consideration the customer actually pays, and VAT is charged on that reduced
 * consideration — so VAT goes <em>outside</em> the discount. Nest it the other way and the
 * company charges tax on money nobody received, which is a decision a developer will have
 * made by choosing a line of code.
 */
public final class ValueAddedTax extends Adjustment {

    private final BigDecimal rate;

    public ValueAddedTax(Charge component, String percent) {
        super(component);
        this.rate = new BigDecimal(percent).movePointLeft(2);
    }

    @Override
    protected BigDecimal adjust(BigDecimal base) {
        return base.add(base.multiply(rate));
    }
}
