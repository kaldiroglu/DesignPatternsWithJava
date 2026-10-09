package dev.kaldiroglu.dp.behavioral.strategy.pricing.solution;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Customer;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Line;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;

/** Prices Ceyda's basket with one till and five rules, and prints the best receipt. */
public class Main {

    public static void main(String[] args) {
        Basket basket = Basket.of(Customer.student("Ceyda"),
                new Line("java-book", "book", Money.of("400.00"), 3));
        CampaignBook today = new CampaignBook(
                new ShelfPrice(),
                PercentageOff.student(),
                PercentageOff.staff(),
                TieredPercentageOff.blackFriday(),
                new CheapestOfEveryThird("book"));

        System.out.println("One till, " + today.size() + " rules, one basket:");
        for (var receipt : today.quoteAll(basket)) {
            System.out.println("  " + receipt);
        }

        Checkout till = new Checkout(today.bestFor(basket));
        System.out.println("The till takes the best rule. Receipt:");
        System.out.println("  " + till.ring(basket));

        till.setRule(PercentageOff.student());
        System.out.println("The same till, given another rule while it runs:");
        System.out.println("  " + till.ring(basket));
    }
}
