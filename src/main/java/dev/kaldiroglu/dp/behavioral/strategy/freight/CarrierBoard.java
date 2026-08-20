package dev.kaldiroglu.dp.behavioral.strategy.freight;

import java.util.ArrayList;
import java.util.List;

/**
 * Every carrier under contract today, and the object that chooses between them.
 * <p>
 * The same shape as {@code pricing.CampaignBook}, in a different domain, and for the same
 * reason: somebody has to decide which strategy is in force, and it should be neither the
 * strategies nor the context. A carrier signed on Monday is a line of configuration here;
 * a carrier dropped is a line removed. No rate card and no desk is touched either way.
 * <p>
 * Note what this class does <em>not</em> do: it never asks a card what kind of card it is.
 * It asks every one of them for a price and compares the numbers.
 */
public final class CarrierBoard {

    private final List<RateCard> cards = new ArrayList<>();

    public CarrierBoard(RateCard... cards) {
        this.cards.addAll(List.of(cards));
    }

    public CarrierBoard add(RateCard card) {
        cards.add(card);
        return this;
    }

    public int size() {
        return cards.size();
    }

    /** What every carrier would charge, in the order they were registered. */
    public List<Quote> quoteAll(Shipment shipment) {
        List<Quote> quotes = new ArrayList<>();
        ShippingDesk desk = new ShippingDesk(cards.getFirst());
        for (RateCard card : cards) {
            desk.setCard(card);              // one desk, every carrier
            quotes.add(desk.book(shipment));
        }
        return List.copyOf(quotes);
    }

    /** The cheapest quote. Ties go to the carrier registered first. */
    public Quote cheapestFor(Shipment shipment) {
        return quoteAll(shipment).stream()
                .min((a, b) -> a.price().compareTo(b.price()))
                .orElseThrow();
    }
}
