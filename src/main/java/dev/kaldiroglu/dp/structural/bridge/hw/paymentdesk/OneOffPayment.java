package dev.kaldiroglu.dp.structural.bridge.hw.paymentdesk;

import java.math.BigDecimal;
import java.util.List;

/** A RefinedAbstraction: authorize, then capture. Once. */
public final class OneOffPayment extends Payment {

    public OneOffPayment(PaymentProvider provider) {
        super(provider);
    }

    @Override
    public List<Receipt> collect(BigDecimal amount) {
        Authorization hold = provider.authorize(amount);
        return List.of(provider.capture(hold));
    }
}
