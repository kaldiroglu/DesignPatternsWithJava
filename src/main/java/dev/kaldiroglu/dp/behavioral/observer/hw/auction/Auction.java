package dev.kaldiroglu.dp.behavioral.observer.hw.auction;

import java.util.ArrayList;
import java.util.List;

/**
 * Homework 2: an auction that tells bidders about every new bid.
 * <p>
 * The homework question: what happens when a bidder decides to stop watching while it is
 * being told about a bid? It calls {@link #unwatch} from inside its own update. If the
 * auction looped over the real list, that would change the list during the loop and throw
 * {@code ConcurrentModificationException}. The auction loops over a copy, so the change
 * takes effect from the next bid.
 */
public final class Auction {

    private final List<BidListener> watchers = new ArrayList<>();
    private int highest;
    private String leader = "nobody";

    public void watch(BidListener watcher) {
        watchers.add(watcher);
    }

    public void unwatch(BidListener watcher) {
        watchers.remove(watcher);
    }

    public void bid(String bidder, int amount) {
        if (amount <= highest) {
            throw new IllegalArgumentException(bidder + " must bid more than " + highest);
        }
        highest = amount;
        leader = bidder;
        for (BidListener watcher : List.copyOf(watchers)) {
            watcher.newBid(this, bidder, amount);
        }
    }

    public int highest() {
        return highest;
    }

    public String leader() {
        return leader;
    }

    public int watcherCount() {
        return watchers.size();
    }
}
