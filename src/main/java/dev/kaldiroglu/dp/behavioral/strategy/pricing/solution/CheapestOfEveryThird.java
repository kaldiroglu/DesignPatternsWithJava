package dev.kaldiroglu.dp.behavioral.strategy.pricing.solution;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Line;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;

/**
 * A <b>ConcreteStrategy</b> that is not a percentage at all: buy two, get the third free.
 * <p>
 * This is the rule that proves the interface is about an <em>algorithm</em> and not about a
 * discount rate. It walks the basket, it groups by category, and it takes off the cheapest
 * of every third item — arithmetic with nothing in common with the rules beside it. Had the
 * interface been {@code int percentOff()}, this campaign could not have been written.
 */
public final class CheapestOfEveryThird implements PricingRule {

    private final String category;

    public CheapestOfEveryThird(String category) {
        this.category = category;
    }

    @Override
    public String name() {
        return "BUY_TWO_GET_ONE";
    }

    @Override
    public Money priceFor(Basket basket) {
        Money off = Money.ZERO;
        for (Line line : basket.inCategory(category)) {
            off = off.plus(line.unitPrice().times(line.quantity() / 3));
        }
        return basket.listTotal().minus(off);
    }
}
