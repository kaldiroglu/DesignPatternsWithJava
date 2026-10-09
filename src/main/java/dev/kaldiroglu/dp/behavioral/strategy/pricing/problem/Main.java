package dev.kaldiroglu.dp.behavioral.strategy.pricing.problem;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Customer;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Line;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Money;

/** Prices Ceyda's basket with the three stages, and shows what each one costs. */
public class Main {

    public static void main(String[] args) {
        Basket basket = Basket.of(Customer.student("Ceyda"),
                new Line("java-book", "book", Money.of("400.00"), 3));

        SwitchingCheckout switching = new SwitchingCheckout();
        System.out.println("Stage one, a switch on a string:");
        for (String campaign : new String[] {"NONE", "STUDENT", "BLACKFRIDAY", "BUY2GET1"}) {
            System.out.println("  " + switching.ring(basket, campaign));
        }
        try {
            switching.ring(basket, "BLACK_FRIDAY");
        } catch (IllegalArgumentException e) {
            System.out.println("  A typo compiles and fails at run time: " + e.getMessage());
        }

        System.out.println("Stage two, a switch on an enum:");
        System.out.println("  " + new EnumCheckout().ring(basket, Campaign.STUDENT));

        System.out.println("Stage three, a checkout class per campaign:");
        System.out.println("  " + new StudentCheckout().ring(basket));
        Till till = new Till();
        System.out.println("  Best for Ceyda: " + till.bestFor(basket));
        System.out.println("  To choose, the till names " + till.campaignsNamedHere()
                + " checkout classes. A new campaign is an edit to the till.");
    }
}
