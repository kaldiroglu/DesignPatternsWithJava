package dev.kaldiroglu.dp.behavioral.strategy.pricing.problem;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Receipt;

/**
 * Stage three: one class per campaign, joined by inheritance.
 * <p>
 * This is the best of the three and the improvement is real. Each rule is its own class,
 * each can be read on its own page and tested on its own, and a new campaign is a new file
 * rather than an edit to a method that already works for five others. Nothing here is
 * stupid; this is where a careful team lands.
 * <p>
 * What it decides quietly is that <b>a till is its campaign</b>. The rule is the object's
 * class, and an object cannot change its class.
 */
public abstract class Checkout {

    /** What to print above the total. */
    protected abstract String campaignName();

    /** What this campaign charges for the basket. */
    protected abstract Money priceFor(Basket basket);

    public final Receipt ring(Basket basket) {
        return new Receipt(campaignName(), basket.listTotal(), priceFor(basket));
    }
}
