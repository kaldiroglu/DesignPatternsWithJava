package dev.kaldiroglu.dp.behavioral.strategy.pricing.solution;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;

/**
 * A <b>ConcreteStrategy</b> with a threshold in it: more off once the basket is big enough.
 * <p>
 * The 1000-lira figure appeared three times across the naive designs and in three different
 * branches. Here it exists once, in the object that means it, and a test can read it back.
 */
public final class TieredPercentageOff implements PricingRule {

    private final String name;
    private final Money threshold;
    private final int belowPercent;
    private final int atOrAbovePercent;

    public TieredPercentageOff(String name, Money threshold,
                               int belowPercent, int atOrAbovePercent) {
        this.name = name;
        this.threshold = threshold;
        this.belowPercent = belowPercent;
        this.atOrAbovePercent = atOrAbovePercent;
    }

    public static PricingRule blackFriday() {
        return new TieredPercentageOff("BLACK_FRIDAY", Money.of("1000.00"), 25, 40);
    }

    public Money threshold() {
        return threshold;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Money priceFor(Basket basket) {
        Money list = basket.listTotal();
        return list.isAtLeast(threshold)
                ? list.percentOff(atOrAbovePercent)
                : list.percentOff(belowPercent);
    }
}
