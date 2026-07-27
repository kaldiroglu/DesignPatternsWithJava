package dev.kaldiroglu.dp.structural.bridge.hw.paymentdesk;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * A RefinedAbstraction: the same amount, taken in equal parts.
 * <p>
 * The rounding rule — every installment equal, the remainder onto the first — is business
 * logic, written once, and correct on every provider including the cash drawer.
 */
public final class InstallmentPlan extends Payment {

    private final int installments;

    public InstallmentPlan(PaymentProvider provider, int installments) {
        super(provider);
        if (installments < 2) {
            throw new IllegalArgumentException("an installment plan needs at least two");
        }
        this.installments = installments;
    }

    @Override
    public List<Receipt> collect(BigDecimal amount) {
        BigDecimal each = amount.divide(BigDecimal.valueOf(installments), 2, RoundingMode.DOWN);
        BigDecimal remainder = amount.subtract(each.multiply(BigDecimal.valueOf(installments)));

        List<Receipt> receipts = new ArrayList<>();
        for (int i = 0; i < installments; i++) {
            BigDecimal due = i == 0 ? each.add(remainder) : each;
            receipts.add(provider.capture(provider.authorize(due)));
        }
        return receipts;
    }
}
