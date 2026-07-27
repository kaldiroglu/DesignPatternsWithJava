# Flyweight — Market-Data Quotes

## Scenario

A trading-floor application processes a market-data feed: hundreds of millions of `Quote` events per day. Each quote carries:

- **Per-event** state: bid, ask, last price, volume, timestamp.
- **Instrument metadata**: symbol, exchange, currency, ISIN, sector, full company name, tick size, lot size, trading hours window.

The metadata for `AAPL` is the *same* for every `AAPL` quote — it doesn't change tick to tick. There are perhaps 10,000 distinct instruments across exchanges; we receive billions of events about them.

## Without the pattern

`Quote` carries the full instrument metadata directly: every event allocates new strings for symbol, exchange, currency, sector, ISIN, company name. Even at modest message rates, this saturates GC, blows the L3 cache, and pushes p99 latency past where the desk will tolerate.

See `problem/Quote.java`.

## With the Flyweight pattern

Split state along the intrinsic/extrinsic boundary:

- **Intrinsic** (immutable, shareable): `Instrument` — symbol, exchange, currency, ISIN, sector, company name, tick size, lot size, trading hours.
- **Extrinsic** (per-event): `Quote` — bid, ask, last, volume, timestamp + a *reference* to an interned `Instrument`.

`InstrumentRegistry` interns `Instrument` instances by composite key. Every `Quote` for `AAPL` shares the same `Instrument` object — one allocation forever.

Hard parts experienced developers must respect:

- **Immutability of intrinsic state** is a contract. A mutable field on `Instrument` would be visible to *every* quote referencing it — a silent global, often a thread-safety bug.
- **Equality and identity** — interning means `==` works for instrument comparison, which is faster but easy to misuse if anyone accidentally constructs an `Instrument` outside the registry.
- **Bounded growth** — the registry must not be a memory leak; if instruments are listed/delisted continuously, you need eviction or a generation strategy.

The demo plants 1,000,000 quotes across 4 instruments and shows that the registry holds exactly 4 `Instrument` objects.

See `solution/`.
