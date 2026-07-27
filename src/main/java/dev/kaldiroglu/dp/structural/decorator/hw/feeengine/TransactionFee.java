package dev.kaldiroglu.dp.structural.decorator.hw.feeengine;

import java.math.BigDecimal;

/** Adds a flat per-transaction charge. */
public final class TransactionFee extends Adjustment {

    private final BigDecimal fee;

    public TransactionFee(Charge component, String fee) {
        super(component);
        this.fee = new BigDecimal(fee);
    }

    @Override
    protected BigDecimal adjust(BigDecimal base) {
        return base.add(fee);
    }
}
