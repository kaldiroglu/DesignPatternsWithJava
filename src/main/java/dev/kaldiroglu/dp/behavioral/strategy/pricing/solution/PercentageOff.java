package dev.kaldiroglu.dp.behavioral.strategy.pricing.solution;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;

import java.util.function.Predicate;

/**
 * A <b>ConcreteStrategy</b> that is really a family of them: a flat percentage, for whoever
 * qualifies.
 * <p>
 * The student and staff campaigns are this class twice with different numbers. That is the
 * first thing to notice about a strategy hierarchy — a rule with parameters is one class,
 * not one class per parameter, and the till cannot tell the difference either way.
 */
public final class PercentageOff implements PricingRule {

    private final String name;
    private final int percent;
    private final Predicate<Basket> qualifies;

    public PercentageOff(String name, int percent, Predicate<Basket> qualifies) {
        this.name = name;
        this.percent = percent;
        this.qualifies = qualifies;
    }

    public static PricingRule student() {
        return new PercentageOff("STUDENT", 20, b -> b.customer().student());
    }

    public static PricingRule staff() {
        return new PercentageOff("STAFF", 30, b -> b.customer().staff());
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Money priceFor(Basket basket) {
        return qualifies.test(basket)
                ? basket.listTotal().percentOff(percent)
                : basket.listTotal();
    }
}
