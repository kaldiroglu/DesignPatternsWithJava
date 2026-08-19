package dev.kaldiroglu.dp.behavioral.strategy.pricing.solution;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;

/**
 * The <b>Strategy</b>: one way of pricing a basket.
 * <p>
 * GoF, p. 315: "Define a family of algorithms, encapsulate each one, and make them
 * interchangeable. Strategy lets the algorithm vary independently from clients that use
 * it."
 * <p>
 * Two methods, and both are about the <em>algorithm</em> rather than about the till. A rule
 * is handed a basket and answers what it charges; it never asks who is asking, never
 * decides whether it should be the rule in force, and never touches a receipt. That is what
 * makes one rule readable on its own page and testable without a checkout.
 * <p>
 * Note what is <em>not</em> here: no {@code appliesTo}, no {@code priority}, no
 * {@code isBetterThan}. Choosing among rules is the context's business, and the moment a
 * rule starts ranking itself against the others, every rule has to know every other rule.
 */
public interface PricingRule {

    /** What to print above the total. */
    String name();

    /** What this rule charges for the basket, whatever the shelf prices add up to. */
    Money priceFor(Basket basket);
}
