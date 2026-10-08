package dev.kaldiroglu.dp.behavioral.observer.hw.auction;

import java.util.ArrayList;
import java.util.List;

/**
 * A <b>ConcreteObserver</b> with a budget. When another bidder goes over its budget, it
 * stops watching — from inside its own update.
 */
public final class Bidder implements BidListener {

    private final String name;
    private final int budget;
    private final List<String> heard = new ArrayList<>();

    public Bidder(String name, int budget) {
        this.name = name;
        this.budget = budget;
    }

    @Override
    public void newBid(Auction auction, String bidder, int amount) {
        heard.add(bidder + " " + amount);
        if (!bidder.equals(name) && amount > budget) {
            heard.add("too much for " + name + ", stops watching");
            auction.unwatch(this);
        }
    }

    public List<String> heard() {
        return List.copyOf(heard);
    }
}
