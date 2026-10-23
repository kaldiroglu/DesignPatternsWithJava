package dev.kaldiroglu.dp.behavioral.strategy.pricing.solution;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;

/** The rule is a field, so the same till can be given another rule while it runs. */
public final class ChangeRuleMain {

    public static void main(String[] args) {
        Basket basket = SampleBasket.build();
        Checkout till = new Checkout(SampleCampaignBook.build().bestFor(basket));
        till.setRule(PercentageOff.student());
        System.out.println("The same till, given another rule while it runs:");
        System.out.println("  " + till.ring(basket));
    }
}
