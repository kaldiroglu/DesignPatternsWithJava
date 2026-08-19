package dev.kaldiroglu.dp.behavioral.strategy.pricing.solution;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Receipt;

import java.util.Objects;

/**
 * The <b>Context</b>: a till, which holds a rule and does not know which one.
 * <p>
 * One field, and it is the whole pattern. The till does not extend a campaign and does not
 * contain a branch over campaigns; it is handed one and asks it for a price. GoF, p. 316:
 * the context forwards requests from its clients to its strategy, and clients usually hand
 * the context the strategy they want.
 * <p>
 * <b>The rule can be replaced on a till that already exists.</b> That is the operation
 * stage three could not perform: there the campaign was the object's class, so the
 * Thursday change meant a different object and a caller that named it.
 */
public final class Checkout {

    private PricingRule rule;

    public Checkout(PricingRule rule) {
        this.rule = Objects.requireNonNull(rule, "a till needs a rule; use ShelfPrice for none");
    }

    /** Point this till at a different campaign. The till itself does not change. */
    public void setRule(PricingRule rule) {
        this.rule = Objects.requireNonNull(rule);
    }

    public String ruleName() {
        return rule.name();
    }

    /**
     * Price one basket and hand back the receipt.
     * <p>
     * Read what is not here: no {@code switch}, no {@code instanceof}, and no campaign name
     * anywhere in the method. The saving is the shelf price less what the rule charged,
     * which is why the receipt's promise costs nothing to keep.
     */
    public Receipt ring(Basket basket) {
        return new Receipt(rule.name(), basket.listTotal(), rule.priceFor(basket));
    }
}
