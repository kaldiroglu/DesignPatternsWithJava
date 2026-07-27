package dev.kaldiroglu.dp.structural.bridge.hw.paymentdesk;

import java.math.BigDecimal;

/** A ConcreteImplementor: a card gateway, genuinely two-phase. */
public final class BankGateway implements PaymentProvider {

    private int counter;

    @Override
    public String name() {
        return "bank";
    }

    @Override
    public Authorization authorize(BigDecimal amount) {
        return new Authorization("BANK-" + (++counter), amount, false);
    }

    @Override
    public Receipt capture(Authorization authorization) {
        return new Receipt(authorization.reference(), authorization.amount(), name());
    }

    @Override
    public Receipt refund(BigDecimal amount, String reference) {
        return new Receipt(reference + "-R", amount.negate(), name());
    }
}
