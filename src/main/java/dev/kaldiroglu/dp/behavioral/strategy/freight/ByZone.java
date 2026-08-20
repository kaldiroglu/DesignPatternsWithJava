package dev.kaldiroglu.dp.behavioral.strategy.freight;

import java.util.Map;

/**
 * A <b>ConcreteStrategy</b>: a base price for the route, plus a rate for every kilo.
 * <p>
 * The international carriers price the distance first and the parcel second, and then add
 * a fuel surcharge that changes with the oil price rather than with anything about the
 * shipment. The surcharge is the reason this card holds state the others do not — it is
 * updated by a feed, not by a contract.
 */
public final class ByZone implements RateCard {

    private final String carrier;
    private final Map<String, Money> zoneBase;
    private final Money perKilo;
    private int fuelSurchargePercent;

    public ByZone(String carrier, Map<String, Money> zoneBase, Money perKilo,
                  int fuelSurchargePercent) {
        this.carrier = carrier;
        this.zoneBase = Map.copyOf(zoneBase);
        this.perKilo = perKilo;
        this.fuelSurchargePercent = fuelSurchargePercent;
    }

    /** The feed moved. No shipment, no quote and no other carrier is affected. */
    public void setFuelSurchargePercent(int percent) {
        this.fuelSurchargePercent = percent;
    }

    @Override
    public String carrier() {
        return carrier;
    }

    @Override
    public Money quote(Shipment shipment) {
        Money base = zoneBase.get(shipment.toZone());
        if (base == null) {
            throw new IllegalArgumentException(carrier + " does not serve " + shipment.toZone());
        }
        int kilos = (shipment.chargeableGrams() + 999) / 1000;
        return base.plus(perKilo.times(kilos)).percentMore(fuelSurchargePercent);
    }
}
