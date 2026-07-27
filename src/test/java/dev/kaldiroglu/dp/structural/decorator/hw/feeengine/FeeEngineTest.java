package dev.kaldiroglu.dp.structural.decorator.hw.feeengine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * The point of this class.
 * <p>
 * One of these two orderings is the company's tax position. The other is a mistake that
 * compiles.
 */
class FeeEngineTest {

    private static Charge subtotal() {
        return new TransactionFee(new PlatformFee(new BasketTotal("100.00"), "8"), "2.50");
    }

    @Test
    @DisplayName("the fees before any discount")
    void subtotalIsAsExpected() {
        assertEquals(new BigDecimal("110.50"), subtotal().amount()); // 100 + 8% + 2.50
    }

    @Test
    @DisplayName("with a fixed voucher the order changes the total, by the VAT on the voucher")
    void aVoucherMakesTheOrderMatter() {
        BigDecimal lawful = new ValueAddedTax(new Voucher(subtotal(), "10.00"), "20").amount();
        BigDecimal unlawful = new Voucher(new ValueAddedTax(subtotal(), "20"), "10.00").amount();

        assertEquals(new BigDecimal("120.60"), lawful);    // (110.50 - 10.00) x 1.20
        assertEquals(new BigDecimal("122.60"), unlawful);  // 110.50 x 1.20 - 10.00
        assertNotEquals(lawful, unlawful);

        // The gap is exactly the VAT on the ten lira the customer never paid.
        assertEquals(new BigDecimal("2.00"), unlawful.subtract(lawful));
        assertEquals(new BigDecimal("2.00"), new BigDecimal("10.00").multiply(new BigDecimal("0.20")).setScale(2));
    }

    @Test
    @DisplayName("with a percentage discount the order changes nothing, because both multiply")
    void aPercentageDiscountCommutes() {
        BigDecimal outside = new ValueAddedTax(new PromotionalDiscount(subtotal(), "10"), "20").amount();
        BigDecimal inside = new PromotionalDiscount(new ValueAddedTax(subtotal(), "20"), "10").amount();

        // 110.50 x 0.90 x 1.20 == 110.50 x 1.20 x 0.90. Multiplication commutes, and the
        // honest lesson is to check each pair rather than recite a rule.
        assertEquals(outside, inside);
        assertEquals(new BigDecimal("119.34"), outside);
    }

    @Test
    @DisplayName("money is kept to two places, half up, at every step")
    void everyStepIsRounded() {
        assertEquals(2, new PlatformFee(new BasketTotal("0.01"), "8").amount().scale());
        assertEquals(2, new ValueAddedTax(new BasketTotal("33.33"), "20").amount().scale());
    }

    @Test
    @DisplayName("a voucher cannot make a charge negative")
    void aVoucherStopsAtZero() {
        assertEquals(new BigDecimal("0.00"), new Voucher(new BasketTotal("5.00"), "10.00").amount());
    }
}
