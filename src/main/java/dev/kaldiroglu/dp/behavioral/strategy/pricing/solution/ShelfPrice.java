package dev.kaldiroglu.dp.behavioral.strategy.pricing.solution;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;

/**
 * A <b>ConcreteStrategy</b>: charge what is on the shelf edge.
 * <p>
 * The one every receipt is measured against, and the reason "no campaign" needs no special
 * case anywhere in {@link Checkout}. GoF call this out as an implementation issue (p. 319):
 * a null strategy forces every client to check for null, so the absence of a campaign is
 * itself a campaign.
 */
public final class ShelfPrice implements PricingRule {

    @Override
    public String name() {
        return "NONE";
    }

    @Override
    public Money priceFor(Basket basket) {
        return basket.listTotal();
    }
}
