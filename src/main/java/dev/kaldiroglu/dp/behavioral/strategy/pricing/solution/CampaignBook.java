package dev.kaldiroglu.dp.behavioral.strategy.pricing.solution;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Receipt;

import java.util.ArrayList;
import java.util.List;

/**
 * Every campaign the store is running today, and the answer to the question stage three
 * could not answer: which of them is best for this basket?
 * <p>
 * Compare with {@code problem.Till}. That class had to name each campaign's type to price a
 * basket several ways. This one holds {@link PricingRule}s and names none of them — the
 * list is handed in, so a campaign added on Thursday is a line of configuration rather than
 * an edit here.
 * <p>
 * This is also where GoF's implementation issue 1 (p. 319) is answered: somebody has to
 * decide which strategy is in force, and it should not be the strategies and it should not
 * be the context. Here it is a small object whose only job is choosing.
 */
public final class CampaignBook {

    private final List<PricingRule> rules = new ArrayList<>();

    public CampaignBook(PricingRule... rules) {
        this.rules.addAll(List.of(rules));
    }

    /** Adding a campaign is one call, and no existing rule or till is touched. */
    public CampaignBook add(PricingRule rule) {
        rules.add(rule);
        return this;
    }

    public int size() {
        return rules.size();
    }

    /** The rule that charges this basket least. Ties go to the rule registered first. */
    public PricingRule bestFor(Basket basket) {
        PricingRule best = rules.getFirst();
        for (PricingRule rule : rules) {
            if (rule.priceFor(basket).compareTo(best.priceFor(basket)) < 0) {
                best = rule;
            }
        }
        return best;
    }

    /** What every campaign would charge, for the "you could have saved more" panel. */
    public List<Receipt> quoteAll(Basket basket) {
        List<Receipt> quotes = new ArrayList<>();
        Checkout till = new Checkout(rules.getFirst());
        for (PricingRule rule : rules) {
            till.setRule(rule);            // one till, every campaign
            quotes.add(till.ring(basket));
        }
        return List.copyOf(quotes);
    }
}
