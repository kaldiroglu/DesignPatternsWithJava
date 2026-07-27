package dev.kaldiroglu.dp.structural.bridge.hw.paymentdesk;

import java.math.BigDecimal;
import java.util.List;

/** A RefinedAbstraction: money going the other way. */
public final class Refund extends Payment {

    private final String originalReference;

    public Refund(PaymentProvider provider, String originalReference) {
        super(provider);
        this.originalReference = originalReference;
    }

    @Override
    public List<Receipt> collect(BigDecimal amount) {
        return List.of(provider.refund(amount, originalReference));
    }
}
