package dev.kaldiroglu.dp.behavioral.strategy.pricing.problem;

/**
 * Marketing asks for the best campaign. At stage three the till needs one checkout per
 * campaign, so it names every checkout class.
 */
public final class BestCampaignMain {

    public static void main(String[] args) {
        Till till = new Till();
        System.out.println("  Best for Ceyda: " + till.bestFor(SampleBasket.build()));
        System.out.println("  To choose, the till names " + till.campaignsNamedHere()
                + " checkout classes. A new campaign is an edit to the till.");
    }
}
