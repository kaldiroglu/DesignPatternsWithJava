package dev.kaldiroglu.dp.structural.decorator.hw.feeengine;

import java.math.BigDecimal;

/** The Component: anything that can state an amount of money. */
public interface Charge {

    BigDecimal amount();
}
