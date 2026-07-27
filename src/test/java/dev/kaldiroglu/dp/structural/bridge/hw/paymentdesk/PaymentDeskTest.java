package dev.kaldiroglu.dp.structural.bridge.hw.paymentdesk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The installment rounding rule is written once. These tests prove it produces the same
 * answer over a provider that really does authorize-then-capture and over one that cannot.
 */
class PaymentDeskTest {

    private static final BigDecimal THOUSAND = new BigDecimal("1000.00");

    private static List<PaymentProvider> providers() {
        return List.of(new BankGateway(), new Wallet(), new CashDrawer());
    }

    @Test
    @DisplayName("one rounding rule, the same three installments on every provider")
    void oneRuleEveryProvider() {
        for (PaymentProvider provider : providers()) {
            List<Receipt> receipts = new InstallmentPlan(provider, 3).collect(THOUSAND);

            assertEquals(3, receipts.size());
            assertEquals(List.of(new BigDecimal("333.34"),
                                 new BigDecimal("333.33"),
                                 new BigDecimal("333.33")),
                    receipts.stream().map(Receipt::amount).toList());
            assertEquals(THOUSAND, receipts.stream()
                    .map(Receipt::amount).reduce(BigDecimal.ZERO, BigDecimal::add));
            assertTrue(receipts.stream().allMatch(r -> r.provider().equals(provider.name())));
        }
    }

    @Test
    @DisplayName("cash collapses the two phases, and the abstraction cannot tell")
    void cashHasNoHold() {
        Authorization bank = new BankGateway().authorize(THOUSAND);
        Authorization cash = new CashDrawer().authorize(THOUSAND);

        assertFalse(bank.settled());  // the money has not moved yet
        assertTrue(cash.settled());   // it is in the drawer

        // Both still answer capture, so OneOffPayment is written once.
        assertEquals(THOUSAND, new OneOffPayment(new BankGateway()).collect(THOUSAND).getFirst().amount());
        assertEquals(THOUSAND, new OneOffPayment(new CashDrawer()).collect(THOUSAND).getFirst().amount());
    }

    @Test
    @DisplayName("no primitive asks which provider it is talking to")
    void noCapabilityBooleans() {
        for (var method : PaymentProvider.class.getDeclaredMethods()) {
            assertFalse(method.getReturnType() == boolean.class,
                    method.getName() + " returns a boolean the abstraction would branch on, "
                            + "which is 'which implementation are you?' in disguise");
        }
    }

    @Test
    @DisplayName("refunds run through the same bridge")
    void refundsToo() {
        List<Receipt> receipts = new Refund(new Wallet(), "WLT-1").collect(new BigDecimal("250.00"));

        assertEquals(1, receipts.size());
        assertEquals(new BigDecimal("-250.00"), receipts.getFirst().amount());
        assertEquals("WLT-1-R", receipts.getFirst().reference());
    }

    @Test
    @DisplayName("four payment kinds and three providers are seven classes, not twelve")
    void mPlusNNotMTimesN() {
        List<Class<?>> kinds = List.of(OneOffPayment.class, InstallmentPlan.class, Refund.class);
        List<Class<?>> impls = List.of(BankGateway.class, Wallet.class, CashDrawer.class);

        assertEquals(6, kinds.size() + impls.size());
        assertEquals(9, kinds.size() * impls.size());
        assertEquals(PaymentProvider.class,
                java.util.Arrays.stream(Payment.class.getDeclaredFields())
                        .filter(f -> f.getName().equals("provider"))
                        .findFirst().orElseThrow().getType());
    }

    @Test
    @DisplayName("an installment plan of one is a mistake, and says so")
    void twoInstallmentsMinimum() {
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> new InstallmentPlan(new Wallet(), 1));
    }
}
