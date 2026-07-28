package dev.kaldiroglu.dp.structural.adapter.hw.payments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Three mismatches, and only one of them is about method names. */
class LegacyGatewayAdapterTest {

    private final LegacyGateway gateway = new LegacyGateway();
    private final PaymentProcessor payments = new LegacyGatewayAdapter(gateway);

    @Test
    @DisplayName("a successful charge returns the gateway's reference")
    void success() {
        assertEquals("TXN-1000", payments.charge("cust-1", new BigDecimal("249.99")));
        assertEquals(1, gateway.callCount());
    }

    @Test
    @DisplayName("lira become cents, rounded half up, once and in one place")
    void amountsAreConverted() {
        payments.charge("cust-1", new BigDecimal("10.005"));   // -> 1001 cents
        payments.charge("cust-1", new BigDecimal("10.004"));   // -> 1000 cents

        assertEquals(2, gateway.callCount(), "both were accepted, so both converted cleanly");
    }

    @Test
    @DisplayName("a status code becomes an exception")
    void codesBecomeExceptions() {
        PaymentDeclinedException thrown = assertThrows(PaymentDeclinedException.class,
                () -> payments.charge("cust-1", new BigDecimal("5000.00")));

        assertEquals(LegacyGateway.INSUFFICIENT_FUNDS, thrown.providerCode());
        assertEquals("insufficient funds", thrown.getMessage());
    }

    @Test
    @DisplayName("an unknown customer is declined, not silently accepted")
    void unknownCustomer() {
        PaymentDeclinedException thrown = assertThrows(PaymentDeclinedException.class,
                () -> payments.charge("", new BigDecimal("10.00")));

        assertEquals(LegacyGateway.UNKNOWN_CUSTOMER, thrown.providerCode());
    }

    @Test
    @DisplayName("a code the adapter has never seen is still a failure")
    void unrecognizedCodesFailClosed() {
        // The riskiest line in any adapter that translates an error model: translating only
        // the codes you know is how a decline becomes a delivered order.
        LegacyGateway odd = new LegacyGateway() {
            @Override
            public void submitTxn(String custId, long amountInCents, String[] out) {
                out[0] = "99";
            }
        };
        PaymentProcessor guarded = new LegacyGatewayAdapter(odd);

        PaymentDeclinedException thrown = assertThrows(PaymentDeclinedException.class,
                () -> guarded.charge("cust-1", new BigDecimal("1.00")));
        assertEquals(99, thrown.providerCode());
        assertTrue(thrown.getMessage().contains("code 99"));
    }

    @Test
    @DisplayName("refunds go through the same translation")
    void refunds() {
        String reference = payments.charge("cust-1", new BigDecimal("10.00"));
        payments.refund(reference, new BigDecimal("10.00"));

        assertThrows(PaymentDeclinedException.class,
                () -> payments.refund("not-a-reference", new BigDecimal("10.00")));
    }
}
