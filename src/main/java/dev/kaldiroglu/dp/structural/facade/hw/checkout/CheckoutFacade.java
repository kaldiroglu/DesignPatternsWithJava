package dev.kaldiroglu.dp.structural.facade.hw.checkout;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The Facade: one call for what used to be four, and one place for the rule nobody wants to
 * re-type.
 * <p>
 * The interesting part of this exercise is not the simplification — it is the <strong>partial
 * failure</strong>. Reserving stock succeeds, then the payment is declined. Somebody has to
 * put the stock back, and if the facade does not, every caller has to know enough about the
 * subsystem to do it themselves. Which is the very knowledge the facade exists to remove.
 * <p>
 * So the compensation lives here. That is a real cost of the pattern and worth naming: a
 * facade that spans several subsystems inherits responsibility for what happens when one of
 * them fails halfway through.
 */
public class CheckoutFacade {

    private final InventoryService inventory;
    private final PaymentService payments;
    private final ShippingService shipping;
    private final ReceiptService receipts;

    public CheckoutFacade(InventoryService inventory, PaymentService payments,
                          ShippingService shipping, ReceiptService receipts) {
        this.inventory = Objects.requireNonNull(inventory);
        this.payments = Objects.requireNonNull(payments);
        this.shipping = Objects.requireNonNull(shipping);
        this.receipts = Objects.requireNonNull(receipts);
    }

    public OrderResult placeOrder(String customerId, String sku, int quantity,
                                  BigDecimal amount, String address) {

        if (!inventory.reserve(sku, quantity)) {
            return OrderResult.failed("out of stock: " + sku);
        }

        String payment;
        try {
            payment = payments.charge(customerId, amount);
        } catch (RuntimeException e) {
            inventory.release(sku, quantity);          // compensate, or leak the reservation
            return OrderResult.failed(e.getMessage());
        }

        String shipment;
        try {
            shipment = shipping.book(sku, quantity, address);
        } catch (RuntimeException e) {
            payments.refund(payment);                  // two things to undo now, in order
            inventory.release(sku, quantity);
            return OrderResult.failed(e.getMessage());
        }

        receipts.email(customerId, payment, shipment);
        return OrderResult.success(payment, shipment);
    }
}
