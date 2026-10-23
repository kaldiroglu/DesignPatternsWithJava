package dev.kaldiroglu.dp.behavioral.strategy.pricing.problem;

/** Stage three: each campaign is a checkout class of its own. */
public final class StageThreeMain {

    public static void main(String[] args) {
        System.out.println("Stage three, a checkout class per campaign:");
        System.out.println("  " + new StudentCheckout().ring(SampleBasket.build()));
    }
}
