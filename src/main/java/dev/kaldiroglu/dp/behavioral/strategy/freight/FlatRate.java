package dev.kaldiroglu.dp.behavioral.strategy.freight;

/**
 * A <b>ConcreteStrategy</b>: one price, whatever it is and wherever it goes.
 * <p>
 * The card a marketplace negotiates for its sellers, and the one that shows a strategy may
 * ignore its input entirely and still be a strategy. It is also the card that wins most
 * often on small parcels and loses badly on large ones, which is the whole reason a
 * comparison exists.
 */
public final class FlatRate implements RateCard {

    private final String carrier;
    private final Money price;

    public FlatRate(String carrier, Money price) {
        this.carrier = carrier;
        this.price = price;
    }

    @Override
    public String carrier() {
        return carrier;
    }

    @Override
    public Money quote(Shipment shipment) {
        return price;
    }
}
