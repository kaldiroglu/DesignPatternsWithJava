/**
 * FLYWEIGHT — Database Connection Pool
 *
 * REAL-WORLD SYSTEM: A database connection pool (HikariCP, Tomcat JDBC pool,
 * C3P0). Creating a TCP connection, performing a TLS handshake, and
 * authenticating to PostgreSQL takes ~5-10ms. A web request that needs three
 * queries would spend 15-30ms just connecting. The pool keeps a small set of
 * pre-established connections and reuses them across thousands of requests.
 *
 * Intrinsic state (shared, immutable): JDBC URL, credentials, pool size.
 * Extrinsic state (per-use, transient): the current transaction, the statement
 * being executed, the result set.
 *
 * SHAPE OF THE PROBLEM: You have many clients that need a small number of
 * expensive-to-create objects. Share them. The clients temporarily "borrow"
 * an object, use it, and return it.
 */
package dev.kaldiroglu.dp.structural.flyweight.pool;

import java.util.concurrent.*;

// ── Demo: 10 concurrent "requests" compete for 3 connections ─────────
public class ConnectionPoolFlyweight {
    public static void main(String[] args) throws Exception {
        var pool = new ConnectionPool(3);

        try (var executor = Executors.newFixedThreadPool(500)) {
            for (int i = 0; i < 10000; i++) {
                final int reqId = i;
                executor.submit(() -> {
                    try {
                        PooledConnection conn = pool.borrow(5);
                        if (conn == null) {
                            System.out.printf("Request #%d TIMED OUT waiting for connection%n", reqId);
                            return;
                        }
                        System.out.printf("Request #%d got conn #%d (available: %d)%n",
                            reqId, conn.id(), pool.availableCount());
                        System.out.printf("  → %s%n", conn.query("SELECT * FROM orders WHERE id=" + reqId));
                        Thread.sleep(50); // simulate query time
                        pool.release(conn);
                        System.out.printf("Request #%d released conn #%d (available: %d)%n",
                            reqId, conn.id(), pool.availableCount());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
        }
    }
}
