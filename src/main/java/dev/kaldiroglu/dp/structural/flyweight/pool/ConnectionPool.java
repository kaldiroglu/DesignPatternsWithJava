package dev.kaldiroglu.dp.structural.flyweight.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

// ── Flyweight factory: the connection pool ───────────────────────────
class ConnectionPool {
    private final BlockingQueue<PooledConnection> available;

    public ConnectionPool(int size) {
        this.available = new LinkedBlockingQueue<>(size);
        for (int i = 0; i < size; i++) {
            available.add(new PooledConnection());
        }
        System.out.println("  [pool] Initialized with " + size + " connections\n");
    }

    public PooledConnection borrow(long timeoutMs) throws InterruptedException {
        PooledConnection conn = available.poll(timeoutMs, TimeUnit.MILLISECONDS);
        if (conn != null) conn.markInUse();
        return conn;
    }

    public void release(PooledConnection conn) {
        conn.markFree();
        available.add(conn);
    }

    public int availableCount() {
        return available.size();
    }
}
