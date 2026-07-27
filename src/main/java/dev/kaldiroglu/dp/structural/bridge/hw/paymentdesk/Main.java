package dev.kaldiroglu.dp.structural.bridge.hw.paymentdesk;

import java.math.BigDecimal;
import java.util.List;

/**
 * Homework 2 — the payment desk.
 * <p>
 * The same {@link InstallmentPlan} code, with its rounding rule, running over a bank gateway
 * and over a cash drawer that has no concept of authorizing anything.
 */
public class Main {

    public static void main(String[] args) {
        BigDecimal amount = new BigDecimal("1000.00");
        List<PaymentProvider> providers = List.of(new BankGateway(), new Wallet(), new CashDrawer());

        System.out.println("A one-off payment of " + amount + ":");
        for (PaymentProvider provider : providers) {
            System.out.println("  " + new OneOffPayment(provider).collect(amount));
        }

        System.out.println("\nThe same 1000.00 in three installments — one rounding rule, three providers:");
        for (PaymentProvider provider : providers) {
            List<Receipt> receipts = new InstallmentPlan(provider, 3).collect(amount);
            BigDecimal sum = receipts.stream()
                    .map(Receipt::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.printf("  %-7s %s  sum %s%n", provider.name(),
                    receipts.stream().map(Receipt::amount).toList(), sum);
        }

        System.out.println("\nCash never authorized anything — its authorize() took the money and");
        System.out.println("its capture() did nothing. InstallmentPlan cannot tell, and must not.");
    }
}
