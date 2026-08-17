package dev.kaldiroglu.dp.structural.proxy.hw.licence;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The seats the university actually bought, and the queue for the next one.
 *
 * <p>Deliberately not a thread pool and deliberately not asleep anywhere: waiting is
 * modelled as a queue that a release drains, so the whole thing is testable without
 * timing. A student who cannot get a seat is enqueued and promoted the moment somebody
 * closes theirs.</p>
 *
 * <p>Worth noticing what this class is <em>not</em>: it is not the solution. It is the
 * resource being rationed. The solution is {@link LicenceProxy}, which stands in front of
 * the application and consults this.</p>
 */
public class LicenceServer {

    private final String product;
    private final int seats;
    private final Set<String> holders = new LinkedHashSet<>();
    private final Deque<String> waiting = new ArrayDeque<>();

    public LicenceServer(String product, int seats) {
        this.product = product;
        this.seats = seats;
    }

    /** Takes a seat if one is free; otherwise joins the queue and returns false. */
    public boolean acquire(String user) {
        if (holders.contains(user)) {
            return true;                       // already holding one; do not double-count
        }
        if (holders.size() < seats) {
            holders.add(user);
            return true;
        }
        if (!waiting.contains(user)) {
            waiting.addLast(user);
        }
        return false;
    }

    /**
     * Gives a seat back and promotes whoever has been waiting longest.
     *
     * @return the user promoted into the freed seat, or null if nobody was waiting
     */
    public String release(String user) {
        if (!holders.remove(user)) {
            return null;
        }
        String next = waiting.pollFirst();
        if (next != null) {
            holders.add(next);
        }
        return next;
    }

    public boolean isHolding(String user) {
        return holders.contains(user);
    }

    public int seats() {
        return seats;
    }

    public int inUse() {
        return holders.size();
    }

    public int available() {
        return seats - holders.size();
    }

    public List<String> queue() {
        return List.copyOf(waiting);
    }

    public String product() {
        return product;
    }
}
