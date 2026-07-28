/**
 * NOT FLYWEIGHT — a database connection pool, kept as a counter-example.
 *
 * <p>Connection pooling is the commonest thing people call Flyweight that is not Flyweight,
 * so this package stays in the repository with its label corrected rather than deleted.
 * See {@code README.md} in this package for the full comparison.</p>
 *
 * <p>The short version: a flyweight is held by many callers <em>at the same time</em> and is
 * immutable, which is what makes that safe. A pooled connection is held by exactly one caller
 * at a time, is mutable, carries a transaction, and must be given back. Flyweight saves
 * memory; a pool saves the 5-10 ms of TCP connect, TLS handshake and authentication.</p>
 */
package dev.kaldiroglu.dp.structural.flyweight.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/** Ten concurrent requests competing for three connections. */
public class Main {

    private static final int REQUESTS = 10;
    private static final int POOL_SIZE = 3;

    public static void main(String[] args) throws Exception {
        ConnectionPool pool = new ConnectionPool(POOL_SIZE);
        AtomicInteger served = new AtomicInteger();
        AtomicInteger timedOut = new AtomicInteger();

        // The previous version said "10 concurrent requests" in a comment and then ran
        // 10,000 of them across 500 threads, printing four lines each.
        ExecutorService executor = Executors.newFixedThreadPool(REQUESTS);
        for (int i = 0; i < REQUESTS; i++) {
            final int requestId = i;
            executor.submit(() -> {
                try {
                    PooledConnection conn = pool.borrow(500);
                    if (conn == null) {
                        timedOut.incrementAndGet();
                        System.out.printf("Request #%d timed out waiting for a connection%n",
                                requestId);
                        return;
                    }
                    try {
                        System.out.printf("Request #%d got conn #%d (available: %d)%n",
                                requestId, conn.id(), pool.availableCount());
                        conn.query("SELECT * FROM orders WHERE id=" + requestId);
                        Thread.sleep(20);
                    } finally {
                        pool.release(conn);   // the half a flyweight never needs
                        served.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        executor.shutdown();
        boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println();
        System.out.println("Finished cleanly     : " + finished);
        System.out.println("Requests served      : " + served.get());
        System.out.println("Requests timed out   : " + timedOut.get());
        System.out.println("Connections created  : " + POOL_SIZE);
        System.out.println("Available at the end : " + pool.availableCount());
        System.out.println();
        System.out.println("Three connections served " + served.get() + " requests - one at a "
                + "time each, and every one had to be given back.");
        System.out.println("A flyweight is held by everybody at once and is never given back.");
    }
}
