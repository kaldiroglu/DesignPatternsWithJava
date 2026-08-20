package dev.kaldiroglu.dp.behavioral.strategy.freight;

/**
 * A <b>ConcreteStrategy</b>: charge for the space the parcel takes, not what it weighs.
 * <p>
 * The domestic carriers rate on desi — volume over three thousand — and bill whichever of
 * desi and actual weight is greater, rounded up to the next unit. A pillow and a paving
 * slab cost the same to send if they fill the same box.
 */
public final class ByDesi implements RateCard {

    private final String carrier;
    private final Money perUnit;
    private final int minimumUnits;

    public ByDesi(String carrier, Money perUnit, int minimumUnits) {
        this.carrier = carrier;
        this.perUnit = perUnit;
        this.minimumUnits = minimumUnits;
    }

    @Override
    public String carrier() {
        return carrier;
    }

    @Override
    public Money quote(Shipment shipment) {
        int units = Math.max(minimumUnits, ceilKilos(shipment.chargeableGrams()));
        return perUnit.times(units);
    }

    private static int ceilKilos(int grams) {
        return (grams + 999) / 1000;
    }
}
