package dev.kaldiroglu.dp.behavioral.strategy.pricing.domain;

/**
 * One line on a basket: a product, its unit price, and how many of it.
 *
 * @param sku       what the product is called on the shelf edge
 * @param category  what the campaign rules group it by
 * @param unitPrice the shelf price of one, before any campaign
 * @param quantity  how many of them
 */
public record Line(String sku, String category, Money unitPrice, int quantity) {

    public Line {
        if (quantity < 1) {
            throw new IllegalArgumentException("a line needs at least one of something");
        }
    }

    /** What this line costs at shelf price, before any campaign touches it. */
    public Money listTotal() {
        return unitPrice.times(quantity);
    }
}
