package dev.kaldiroglu.dp.behavioral.strategy.pricing.problem;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;

/** Twenty percent off, for anybody who showed a student card. */
public final class StudentCheckout extends Checkout {

    @Override
    protected String campaignName() {
        return "STUDENT";
    }

    @Override
    protected Money priceFor(Basket basket) {
        return basket.customer().student()
                ? basket.listTotal().percentOff(20)
                : basket.listTotal();
    }
}
