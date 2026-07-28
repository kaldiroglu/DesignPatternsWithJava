package dev.kaldiroglu.dp.structural.facade.hw.checkout;

/** A subsystem class. */
public class ShippingService {

    private int bookings;

    public String book(String sku, int quantity, String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("no delivery address");
        }
        bookings++;
        return "SHIP-" + bookings;
    }

    public int bookings() {
        return bookings;
    }
}
