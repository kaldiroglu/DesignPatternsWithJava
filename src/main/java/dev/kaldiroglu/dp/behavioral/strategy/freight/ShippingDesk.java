package dev.kaldiroglu.dp.behavioral.strategy.freight;

import java.util.Objects;

/**
 * The <b>Context</b>: books a shipment with whichever carrier it has been given.
 * <p>
 * One field, and no branch. The desk does not know how the price was reached and cannot
 * find out — which is what lets a carrier be added, repriced or dropped without this class
 * being opened.
 */
public final class ShippingDesk {

    private RateCard card;

    public ShippingDesk(RateCard card) {
        this.card = Objects.requireNonNull(card, "a desk needs a carrier");
    }

    /** Contracts change. The desk does not. */
    public void setCard(RateCard card) {
        this.card = Objects.requireNonNull(card);
    }

    public String carrier() {
        return card.carrier();
    }

    public Quote book(Shipment shipment) {
        return new Quote(card.carrier(), card.quote(shipment));
    }
}
