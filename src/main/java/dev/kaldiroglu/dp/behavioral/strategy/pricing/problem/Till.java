package dev.kaldiroglu.dp.behavioral.strategy.pricing.problem;

import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Basket;
import dev.kaldiroglu.dp.behavioral.strategy.pricing.domain.Receipt;

import java.util.ArrayList;
import java.util.List;

/**
 * The reversal: what stage three costs the moment the store asks for the obvious thing.
 * <p>
 * The promise the story opened with is that the customer is given the <em>best</em> campaign
 * they qualify for, and the receipt says what it saved them. Both need one basket priced
 * more than one way. In {@code solution} that is a loop over rules. Here the campaign is the
 * object's class, so pricing a basket five ways means constructing five tills — and this
 * class, which is the caller, has to name every campaign in the company to do it.
 * <p>
 * <b>Read the field.</b> Adding a campaign now edits this file too, which is the thing
 * stage three was supposed to have fixed. The branch did not go away; it moved, and turned
 * into a list of type names.
 */
public final class Till {

    // Every concrete campaign, named in the caller. GoF, p. 316: clients that choose an
    // algorithm this way are coupled to the class of every algorithm they might choose.
    private final List<Checkout> everyCampaign = List.of(
            new PlainCheckout(),
            new StudentCheckout(),
            new BlackFridayCheckout());

    /** The best receipt this basket can be given, and the campaign that produced it. */
    public Receipt bestFor(Basket basket) {
        List<Receipt> quotes = new ArrayList<>();
        for (Checkout checkout : everyCampaign) {
            quotes.add(checkout.ring(basket));
        }
        return quotes.stream().min((a, b) -> a.paid().compareTo(b.paid())).orElseThrow();
    }

    /** How many campaign classes this caller had to know about to do that. */
    public int campaignsNamedHere() {
        return everyCampaign.size();
    }
}
