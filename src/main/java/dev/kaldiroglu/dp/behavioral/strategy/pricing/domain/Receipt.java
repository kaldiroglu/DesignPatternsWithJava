package dev.kaldiroglu.dp.behavioral.strategy.pricing.domain;

/**
 * What the customer is handed, and the promise the whole example turns on.
 * <p>
 * The receipt names the campaign that was applied and states what it saved. Both require
 * the same basket to be priced <em>twice</em> — once at shelf price and once under the
 * campaign — which is the operation the third naive design cannot perform at any price.
 *
 * @param campaign what to print above the total
 * @param list     the shelf price of the basket
 * @param paid     what the customer actually pays
 */
public record Receipt(String campaign, Money list, Money paid) {

    /** What the campaign took off. Zero when no campaign applied. */
    public Money saved() {
        return list.minus(paid);
    }

    @Override
    public String toString() {
        return "%-22s list %8s   paid %8s   saved %8s"
                .formatted(campaign, list, paid, saved());
    }
}
