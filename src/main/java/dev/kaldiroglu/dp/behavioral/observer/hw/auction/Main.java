package dev.kaldiroglu.dp.behavioral.observer.hw.auction;

/**
 * Two bidders watch an auction. When a bid passes Deniz's budget, Deniz stops watching
 * from inside the update. The auction loops over a copy of its list, so nothing fails.
 */
public final class Main {

    public static void main(String[] args) {
        Auction auction = new Auction();
        Bidder ayse = new Bidder("Ayse", 500);
        Bidder deniz = new Bidder("Deniz", 200);
        auction.watch(ayse);
        auction.watch(deniz);

        auction.bid("Deniz", 150);
        auction.bid("Ayse", 250);
        auction.bid("Ayse", 300);

        System.out.println("Ayse heard:  " + ayse.heard());
        System.out.println("Deniz heard: " + deniz.heard());
        System.out.println("Leader: " + auction.leader() + " at " + auction.highest()
                + ", watchers left: " + auction.watcherCount());
    }
}
