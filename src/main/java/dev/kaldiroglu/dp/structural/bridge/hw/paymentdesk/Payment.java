package dev.kaldiroglu.dp.structural.bridge.hw.paymentdesk;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * The Abstraction: a kind of payment, over whatever provider it was handed.
 * <p>
 * Four payment kinds and three providers are seven classes, not twelve. Nothing below this
 * line mentions a bank, a wallet or a drawer.
 */
public abstract class Payment {

    protected final PaymentProvider provider;

    protected Payment(PaymentProvider provider) {
        this.provider = Objects.requireNonNull(provider);
    }

    /** Takes the money, however this kind of payment takes it. */
    public abstract List<Receipt> collect(BigDecimal amount);
}
