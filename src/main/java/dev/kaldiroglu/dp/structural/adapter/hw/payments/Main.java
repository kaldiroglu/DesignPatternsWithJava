package dev.kaldiroglu.dp.structural.adapter.hw.payments;

import java.math.BigDecimal;

/** Homework 1 — a gateway from 2004 behind a modern interface. */
public class Main {

    public static void main(String[] args) {
        LegacyGateway gateway = new LegacyGateway();
        PaymentProcessor payments = new LegacyGatewayAdapter(gateway);

        System.out.println("charged, reference: " + payments.charge("cust-1", new BigDecimal("249.99")));

        try {
            payments.charge("cust-1", new BigDecimal("5000.00"));
        } catch (PaymentDeclinedException e) {
            System.out.println("declined: " + e.getMessage() + " (code " + e.providerCode() + ")");
        }

        try {
            payments.charge("", new BigDecimal("10.00"));
        } catch (PaymentDeclinedException e) {
            System.out.println("declined: " + e.getMessage() + " (code " + e.providerCode() + ")");
        }

        System.out.println("""

                The application deals in BigDecimal and exceptions. The gateway deals
                in integer cents, status codes and an out-parameter array.

                Only one of those three mismatches was about method names.""");
    }
}
