package dev.kaldiroglu.dp.structural.decorator.hw.feeengine;

import java.math.BigDecimal;

/**
 * Takes a fixed amount off — a ten-lira voucher, not a percentage.
 * <p>
 * This class is the one that makes the ordering question real, and the reason is arithmetic
 * rather than tax law. A <em>percentage</em> discount and a percentage VAT are both
 * multiplications, and multiplication commutes: {@code base x 0.90 x 1.20} equals
 * {@code base x 1.20 x 0.90} exactly, so with {@link PromotionalDiscount} the two orderings
 * agree to the kuruş and there is nothing to argue about.
 * <p>
 * Subtracting a fixed amount does not commute with multiplying. The gap between the two
 * orderings is precisely the VAT on the voucher, and that is money somebody either owes or
 * does not.
 */
public final class Voucher extends Adjustment {

    private final BigDecimal value;

    public Voucher(Charge component, String value) {
        super(component);
        this.value = new BigDecimal(value);
    }

    @Override
    protected BigDecimal adjust(BigDecimal base) {
        return base.subtract(value).max(BigDecimal.ZERO);
    }
}
