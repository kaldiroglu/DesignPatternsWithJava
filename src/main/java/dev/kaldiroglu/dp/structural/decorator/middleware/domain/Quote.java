package dev.kaldiroglu.dp.structural.decorator.middleware.domain;

import java.math.BigDecimal;

/** A supplier's price for one item. */
public record Quote(String sku, BigDecimal amount, String currency) {

    public static Quote of(String sku, String amount) {
        return new Quote(sku, new BigDecimal(amount), "EUR");
    }

    @Override
    public String toString() {
        return sku + " = " + amount + " " + currency;
    }
}
