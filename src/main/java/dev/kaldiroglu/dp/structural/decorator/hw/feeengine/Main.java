package dev.kaldiroglu.dp.structural.decorator.hw.feeengine;

/**
 * Homework 2 — the fee engine.
 * <p>
 * Four adjustments, one amount, and an ordering question whose answer comes from tax law.
 * The second half of the demonstration is the honest one: with a <em>percentage</em>
 * discount the order makes no difference at all, and knowing which case you are in is the
 * skill being taught.
 */
public class Main {

    public static void main(String[] args) {
        Charge basket = new BasketTotal("100.00");
        Charge subtotal = new TransactionFee(new PlatformFee(basket, "8"), "2.50");
        System.out.println("basket                                  " + basket.amount());
        System.out.println("after platform fee and transaction fee  " + subtotal.amount());
        System.out.println();

        // A ten-lira voucher: subtraction, which does not commute with multiplication.
        Charge lawful = new ValueAddedTax(new Voucher(subtotal, "10.00"), "20");
        Charge unlawful = new Voucher(new ValueAddedTax(subtotal, "20"), "10.00");

        System.out.println("VOUCHER — a fixed amount off");
        System.out.println("  VAT outside the voucher   (lawful)    " + lawful.amount());
        System.out.println("  VAT inside the voucher    (not)       " + unlawful.amount());
        System.out.println("  difference                            "
                + unlawful.amount().subtract(lawful.amount())
                + "   <- the VAT on the voucher itself");
        System.out.println();

        // A percentage discount: multiplication, which does commute.
        Charge percentOutside = new ValueAddedTax(new PromotionalDiscount(subtotal, "10"), "20");
        Charge percentInside = new PromotionalDiscount(new ValueAddedTax(subtotal, "20"), "10");

        System.out.println("PERCENTAGE — 10% off");
        System.out.println("  VAT outside the discount              " + percentOutside.amount());
        System.out.println("  VAT inside the discount               " + percentInside.amount());
        System.out.println("  difference                            "
                + percentInside.amount().subtract(percentOutside.amount())
                + "   <- none: both are multiplications");
        System.out.println();
        System.out.println("The lesson is not 'order always matters'. It is that order is a");
        System.out.println("question you must ask of each pair — and that when it does matter,");
        System.out.println("a developer choosing a line of code chose the company's tax position.");
    }
}
