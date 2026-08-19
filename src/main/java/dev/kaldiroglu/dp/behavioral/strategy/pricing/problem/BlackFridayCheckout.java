package dev.kaldiroglu.dp.behavioral.strategy.pricing.problem;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;

/** Twenty-five percent off, or forty over a thousand lira. */
public final class BlackFridayCheckout extends Checkout {

    @Override
    protected String campaignName() {
        return "BLACK_FRIDAY";
    }

    @Override
    protected Money priceFor(Basket basket) {
        Money list = basket.listTotal();
        return list.isAtLeast(Money.of("1000.00")) ? list.percentOff(40) : list.percentOff(25);
    }
}
