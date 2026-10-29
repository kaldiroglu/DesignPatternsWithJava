package dev.kaldiroglu.dp.introduction.problems.pricing;

import dev.kaldiroglu.dp.introduction.problems.pricing.domain.Basket;

/** Stage one: the campaign arrives as a string, and a typo fails only at run time. */
public final class StageOneMain {

    public static void main(String[] args) {
        Basket basket = SampleBasket.build();
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
    }
}
