package dev.kaldiroglu.dp.behavioral.strategy.pricing.problem;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;

/** Shelf price, no campaign. */
public final class PlainCheckout extends Checkout {

    @Override
    protected String campaignName() {
        return "NONE";
    }

    @Override
    protected Money priceFor(Basket basket) {
        return basket.listTotal();
    }
}
