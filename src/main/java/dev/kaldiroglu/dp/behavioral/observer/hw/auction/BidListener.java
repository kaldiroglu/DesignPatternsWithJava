package dev.kaldiroglu.dp.behavioral.observer.hw.auction;

/** The <b>Observer</b>: a bidder who wants to know about new bids. */
public interface BidListener {

    void newBid(Auction auction, String bidder, int amount);
}
