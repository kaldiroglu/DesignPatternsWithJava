package dev.kaldiroglu.dp.structural.adapter.hw.payments;

/** How this application says a payment did not happen. */
public class PaymentDeclinedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final int providerCode;

    public PaymentDeclinedException(String message, int providerCode) {
        super(message);
        this.providerCode = providerCode;
    }

    public int providerCode() {
        return providerCode;
    }
}
