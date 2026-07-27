package dev.kaldiroglu.dp.structural.decorator.hw.feeengine;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** The ConcreteComponent: what the customer put in the basket, before anyone adjusted it. */
public final class BasketTotal implements Charge {

    private final BigDecimal amount;

    public BasketTotal(String amount) {
        this.amount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal amount() {
        return amount;
    }
}
