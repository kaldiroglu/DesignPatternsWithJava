package dev.kaldiroglu.dp.structural.facade.hw.checkout;

import java.math.BigDecimal;

/** Homework 1 — checkout, and what a facade owes you when a step fails. */
public class Main {

    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        PaymentService payments = new PaymentService();
        ShippingService shipping = new ShippingService();
        ReceiptService receipts = new ReceiptService();
        CheckoutFacade checkout = new CheckoutFacade(inventory, payments, shipping, receipts);

        System.out.println(checkout.placeOrder("c-1", "SKU-100", 1,
                new BigDecimal("49.90"), "Kadikoy, Istanbul"));

        System.out.println(checkout.placeOrder("c-2", "SKU-200", 1,
                new BigDecimal("10.00"), "Kadikoy, Istanbul"));

        System.out.println(checkout.placeOrder("c-3", "SKU-100", 1,
                new BigDecimal("5000.00"), "Kadikoy, Istanbul"));

        System.out.printf("%nreservations still held : %d%n", inventory.reservations());
        System.out.printf("charges / refunds       : %d / %d%n",
                payments.charges(), payments.refunds());
        System.out.printf("receipts sent           : %d%n", receipts.sent().size());
        System.out.println("""

                One reservation, for the order that succeeded. The declined payment
                did not leave stock held — the facade put it back.

                That is the part of this exercise worth arguing about: a facade over
                several subsystems inherits responsibility for partial failure, and
                the alternative is every caller knowing how to undo a reservation.""");
    }
}
