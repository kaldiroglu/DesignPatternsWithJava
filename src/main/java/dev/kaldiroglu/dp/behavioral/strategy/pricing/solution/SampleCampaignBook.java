package dev.kaldiroglu.dp.behavioral.strategy.pricing.solution;

/** Today's five campaigns. This is the one place that names the concrete rules. */
final class SampleCampaignBook {

    private SampleCampaignBook() {
    }

    static CampaignBook build() {
        return new CampaignBook(
                new ShelfPrice(),
                PercentageOff.student(),
                PercentageOff.staff(),
                TieredPercentageOff.blackFriday(),
                new CheapestOfEveryThird("book"));
    }
}
