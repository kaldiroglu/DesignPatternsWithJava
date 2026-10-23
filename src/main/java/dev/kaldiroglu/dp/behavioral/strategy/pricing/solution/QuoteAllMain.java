package dev.kaldiroglu.dp.behavioral.strategy.pricing.solution;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;

/** One till prices one basket with all five rules. */
public final class QuoteAllMain {

    public static void main(String[] args) {
        Basket basket = SampleBasket.build();
        CampaignBook today = SampleCampaignBook.build();
        System.out.println("One till, " + today.size() + " rules, one basket:");
        for (var receipt : today.quoteAll(basket)) {
            System.out.println("  " + receipt);
        }
    }
}
