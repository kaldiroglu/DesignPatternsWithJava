package dev.kaldiroglu.dp.structural.flyweight.pool;

import java.util.concurrent.atomic.AtomicInteger;

// ── The flyweight object — expensive to create ───────────────────────
class PooledConnection implements AutoCloseable {
    private static final AtomicInteger SEQUENCE = new AtomicInteger(1);
    private final int id;
    private boolean inUse;

    public PooledConnection() {
        this.id = SEQUENCE.getAndIncrement();
        // Real system: TCP connect + TLS handshake + PostgreSQL auth
        System.out.println("  [pool] Created connection #" + id + " (expensive!)");
    }

    public int id() {
        return id;
    }

    public boolean inUse() {
        return inUse;
    }

    public void markInUse() {
        this.inUse = true;
    }

    public void markFree() {
        this.inUse = false;
    }

    public String query(String sql) {
        // Real system: send query over TCP, read result set
        return "result of '" + sql + "' via conn #" + id;
    }

    @Override
    public void close() {
        markFree();
    }
}
