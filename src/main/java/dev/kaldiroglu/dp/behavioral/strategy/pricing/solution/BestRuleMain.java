package dev.kaldiroglu.dp.behavioral.strategy.pricing.solution;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;

/** The campaign book chooses the best rule, and the till uses it. */
public final class BestRuleMain {

    public static void main(String[] args) {
        Basket basket = SampleBasket.build();
        Checkout till = new Checkout(SampleCampaignBook.build().bestFor(basket));
        System.out.println("The till takes the best rule. Receipt:");
        System.out.println("  " + till.ring(basket));
    }
}
