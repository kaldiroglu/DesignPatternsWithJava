package dev.kaldiroglu.dp.behavioral.strategy.freight;

import java.util.List;

/**
 * A <b>ConcreteStrategy</b>: a printed table of weight bands, and a price for each.
 * <p>
 * Not a rate at all — a lookup. The price does not rise smoothly with weight; it steps, and
 * a parcel one gram over a band edge costs a whole band more. This is the card that would
 * not survive an interface shaped as {@code Money perKilo()}.
 */
public final class ByWeightBand implements RateCard {

    /**
     * One row of the table.
     *
     * @param upToGrams the top of the band, inclusive
     * @param price     what anything in this band costs
     */
    public record Band(int upToGrams, Money price) {
    }

    private final String carrier;
    private final List<Band> bands;
    private final Money overflowPrice;

    public ByWeightBand(String carrier, List<Band> bands, Money overflowPrice) {
        this.carrier = carrier;
        this.bands = bands.stream()
                .sorted((a, b) -> Integer.compare(a.upToGrams(), b.upToGrams()))
                .toList();
        this.overflowPrice = overflowPrice;
    }

    @Override
    public String carrier() {
        return carrier;
    }

    @Override
    public Money quote(Shipment shipment) {
        int grams = shipment.chargeableGrams();
        for (Band band : bands) {
            if (grams <= band.upToGrams()) {
                return band.price();
            }
        }
        return overflowPrice;
    }
}
