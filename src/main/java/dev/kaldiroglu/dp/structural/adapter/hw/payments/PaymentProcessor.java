package dev.kaldiroglu.dp.structural.adapter.hw.payments;

import java.math.BigDecimal;

/**
 * The Target: what our application expects of anything that takes money.
 * <p>
 * Modern in two ways that matter — it deals in {@code BigDecimal}, and it reports failure by
 * <strong>throwing</strong> rather than by returning a code.
 */
public interface PaymentProcessor {

    /** @return the provider's reference for the payment. */
    String charge(String customerId, BigDecimal amount);

    void refund(String reference, BigDecimal amount);
}
