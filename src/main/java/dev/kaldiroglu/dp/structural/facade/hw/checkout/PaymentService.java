package dev.kaldiroglu.dp.structural.facade.hw.checkout;

import java.math.BigDecimal;

/** A subsystem class. */
public class PaymentService {

    private int charges;
    private int refunds;

    public String charge(String customerId, BigDecimal amount) {
        if (amount.compareTo(new BigDecimal("1000")) > 0) {
            throw new IllegalStateException("payment declined: over the limit");
        }
        charges++;
        return "PAY-" + charges;
    }

    public void refund(String reference) {
        refunds++;
    }

    public int charges() {
        return charges;
    }

    public int refunds() {
        return refunds;
    }
}
