package dev.kaldiroglu.dp.structural.bridge.hw.paymentdesk;

import java.math.BigDecimal;

/**
 * A ConcreteImplementor: notes and coins, at the desk.
 * <p>
 * There is no hold and no later settlement. {@code authorize} takes the money and says so by
 * returning an authorization already marked settled; {@code capture} therefore has nothing to
 * do and returns the receipt for what already happened. The abstraction above calls both, in
 * order, exactly as it does for the bank — and never learns that one of the two calls did
 * nothing.
 */
public final class CashDrawer implements PaymentProvider {

    private int counter;

    @Override
    public String name() {
        return "cash";
    }

    @Override
    public Authorization authorize(BigDecimal amount) {
        return new Authorization("CASH-" + (++counter), amount, true);
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
