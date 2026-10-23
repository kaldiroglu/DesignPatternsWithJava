package dev.kaldiroglu.dp.behavioral.strategy.pricing.problem;

/** Stage two: the campaign is an enum, and the switch over it is exhaustive. */
public final class StageTwoMain {

    public static void main(String[] args) {
        System.out.println("Stage two, a switch on an enum:");
        System.out.println("  " + new EnumCheckout().ring(SampleBasket.build(), Campaign.STUDENT));
    }
}
