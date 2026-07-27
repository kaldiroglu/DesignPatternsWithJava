# Flyweight — Database Connection Pool (Java)

## Real-world context

A database connection pool — HikariCP, Tomcat JDBC pool, C3P0. Creating a
TCP connection, performing a TLS handshake, and authenticating to
PostgreSQL takes ~5-10ms. A web request that needs three queries would
spend 15-30ms just connecting. The pool keeps a small set of
pre-established connections and reuses them across thousands of requests.

## Class roles

| Class | Role | Why it exists |
|-------|------|---------------|
| `PooledConnection` | **Flyweight** | The expensive, reusable object. In the real system: a `java.sql.Connection` wrapping a TCP socket, TLS session, and authenticated database session. |
| `ConnectionPool` | **Flyweight factory** | Creates the initial set of connections, manages borrow/release, enforces pool size limits. |
| `BlockingQueue` | **Internal data structure** | Thread-safe queue for available connections. `poll()` blocks with timeout — this is how callers wait when the pool is exhausted. |

## Intrinsic vs. extrinsic state

| State | Type | Examples |
|-------|------|----------|
| **Intrinsic** | Shared, immutable, set at creation | JDBC URL, database name, credentials, pool size, socket parameters |
| **Extrinsic** | Per-use, transient, passed by caller | Current transaction, the SQL statement being executed, the `ResultSet`, query timeout |

The flyweight (`PooledConnection`) stores intrinsic state internally.
Extrinsic state is passed *to* the flyweight each time it's used (via
`query(sql)`) and cleared when the connection is returned to the pool.

## How the pattern is implemented

1. **Pool as factory.** `ConnectionPool` creates `n` connections in its constructor — this is the expensive part. After initialization, no more connections are created.

2. **Borrow/release lifecycle.** Callers call `borrow()` to get a connection and `release()` to return it. `PooledConnection` implements `AutoCloseable` so it can be used in try-with-resources — `close()` calls `markFree()`.

3. **Thread safety.** `BlockingQueue` handles concurrent access. Multiple threads can borrow and release simultaneously without external synchronization.

4. **Timeout handling.** `borrow(timeoutMs)` uses `poll()` with a timeout. If no connection is available within the timeout, it returns `null` — the caller handles the failure.

## What to look for in the code

- The constructor prints "expensive!" — in the real system this is where TCP+TLS happens
- `borrow()` marks the connection in-use before returning it
- `release()` marks it free and puts it back in the queue
- The demo spawns 10 threads competing for 3 connections — some will time out
- Each connection has a unique `id` — you can trace which request got which connection
