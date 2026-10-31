package dev.kaldiroglu.dp.structural.flyweight.pool;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

// ── The flyweight object — expensive to create ───────────────────────
class PooledConnection implements AutoCloseable {
    private static final AtomicInteger SEQUENCE = new AtomicInteger(1);
    private final int id;
    private final ConnectionPool pool;
    private final AtomicBoolean inUse = new AtomicBoolean();

    PooledConnection(ConnectionPool pool) {
        this.pool = pool;
        this.id = SEQUENCE.getAndIncrement();
        // Real system: TCP connect + TLS handshake + PostgreSQL auth
        System.out.println("  [pool] Created connection #" + id + " (expensive!)");
    }

    public int id() {
        return id;
    }

    public boolean inUse() {
        return inUse.get();
    }

    public void markInUse() {
        inUse.set(true);
    }

    /** Returns true only for the call that actually freed it, so a second release does nothing. */
    public boolean markFree() {
        return inUse.compareAndSet(true, false);
    }

    public String query(String sql) {
        // Real system: send query over TCP, read result set
        return "result of '" + sql + "' via conn #" + id;
    }

    /** Gives this connection back to its pool, so try-with-resources cannot leak it. */
    @Override
    public void close() {
        pool.release(this);
    }
}
