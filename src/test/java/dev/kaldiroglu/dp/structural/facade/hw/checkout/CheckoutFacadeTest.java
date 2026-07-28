package dev.kaldiroglu.dp.structural.facade.hw.checkout;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** One call instead of four — and what the facade owes you when step three fails. */
class CheckoutFacadeTest {

    private InventoryService inventory;
    private PaymentService payments;
    private ShippingService shipping;
    private ReceiptService receipts;
    private CheckoutFacade checkout;

    @BeforeEach
    void setUp() {
        inventory = new InventoryService();
        payments = new PaymentService();
        shipping = new ShippingService();
        receipts = new ReceiptService();
        checkout = new CheckoutFacade(inventory, payments, shipping, receipts);
    }

    @Test
    @DisplayName("the happy path is one call across four subsystems")
    void happyPath() {
        OrderResult result = checkout.placeOrder("c-1", "SKU-100", 1,
                new BigDecimal("49.90"), "Kadikoy");

        assertTrue(result.placed());
        assertEquals("PAY-1", result.paymentReference());
        assertEquals("SHIP-1", result.shipmentReference());
        assertEquals(1, receipts.sent().size());
    }

    @Test
    @DisplayName("a failure before anything happened touches nothing else")
    void outOfStock() {
        OrderResult result = checkout.placeOrder("c-1", "SKU-200", 1,
                new BigDecimal("10.00"), "Kadikoy");

        assertFalse(result.placed());
        assertEquals(0, payments.charges(), "no payment was attempted");
        assertEquals(0, inventory.reservations());
    }

    @Test
    @DisplayName("a declined payment releases the stock the facade had already reserved")
    void paymentDeclinedCompensates() {
        OrderResult result = checkout.placeOrder("c-1", "SKU-100", 1,
                new BigDecimal("5000.00"), "Kadikoy");

        assertFalse(result.placed());
        assertTrue(result.failure().contains("declined"));
        // The point of the exercise: without this, the reservation leaks and every caller
        // would need to know how to undo it.
        assertEquals(0, inventory.reservations(), "the reservation was given back");
    }

    @Test
    @DisplayName("a shipping failure undoes both earlier steps, in order")
    void shippingFailureCompensatesTwice() {
        OrderResult result = checkout.placeOrder("c-1", "SKU-100", 1,
                new BigDecimal("49.90"), "   ");

        assertFalse(result.placed());
        assertEquals(1, payments.charges());
        assertEquals(1, payments.refunds(), "the charge was refunded");
        assertEquals(0, inventory.reservations(), "and the stock released");
        assertEquals(0, receipts.sent().size(), "and no receipt went out");
    }

    @Test
    @DisplayName("the client names the facade and one result type — nothing else")
    void theClientNamesNothingFromTheSubsystem() {
        // placeOrder takes only String, int and BigDecimal, and returns OrderResult.
        var method = java.util.Arrays.stream(CheckoutFacade.class.getDeclaredMethods())
                .filter(m -> m.getName().equals("placeOrder")).findFirst().orElseThrow();

        for (Class<?> p : method.getParameterTypes()) {
            assertFalse(p.getPackageName().equals(CheckoutFacade.class.getPackageName())
                            && !p.equals(OrderResult.class),
                    "a subsystem type leaked into the signature: " + p.getSimpleName());
        }
        assertEquals(OrderResult.class, method.getReturnType());
    }
}
