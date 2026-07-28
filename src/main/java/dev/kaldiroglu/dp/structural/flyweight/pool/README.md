# Not Flyweight — an Object Pool

**Claude Opus 5 (1M context) · July 28, 2026**

This package is kept as a **counter-example**. It was written and documented as an
implementation of Flyweight, and it is not one. Connection pooling is the single most
common thing people call Flyweight that isn't, which makes it worth keeping and worth
being explicit about.

## The claim that was here before

The earlier README labelled `PooledConnection` as the *Flyweight* participant and
`ConnectionPool` as the *FlyweightFactory*. Both labels are wrong, and the code shows why.

## Why it is not Flyweight

| | Flyweight | Object pool (this package) |
|---|---|---|
| How many hold one object at once | **Many**, simultaneously | **One**, exclusively |
| Mutable? | No — immutable is what makes sharing safe | Yes — it carries a transaction, a cursor, a state |
| Given back? | Never. There is nothing to give back | Always. `release()` is the whole protocol |
| What it saves | **Memory** — one copy of repeated state | **Time** — one expensive setup, reused |
| If you forget to return it | Nothing. Nobody returns a flyweight | The pool leaks and eventually blocks |
| Runs out? | No | Yes — `borrow` can time out and return null |

The deciding question is the first row. A flyweight is shared *concurrently*: the letter
`e` is in a thousand places in a document at the same instant, and that is safe precisely
because it is immutable and holds no context. A pooled connection is handed to exactly one
caller at a time, and handing it to two would be a bug — it carries a transaction.

The second deciding question is what is being saved. Flyweight is a memory pattern; GoF
put it in the structural chapter and its whole argument is object *count*. A pool is a
latency pattern. They both involve a factory handing out pre-existing objects, and that
resemblance is where the confusion comes from.

## What it is instead

Object Pool — not a GoF pattern. It appears in Grand's *Patterns in Java* and as
"Resource Pool" elsewhere. In production you meet it as HikariCP, Tomcat JDBC, C3P0,
Apache Commons Pool, and every thread pool you have used.

`PooledConnection` is a **Proxy** as well, in the smart-reference sense GoF describe on
p. 208: `close()` means *I am finished with it*, not *close the socket*.

## What to take from it

If you are about to call something a flyweight, ask whether two holders at the same time
is correct or catastrophic. If catastrophic, it is a pool.

## Run it with

```bash
mvn -q compile
mvn -q exec:java -Dexec.mainClass=dev.kaldiroglu.dp.structural.flyweight.pool.Main
mvn -q test -Dtest=PoolIsNotFlyweightTest
```
