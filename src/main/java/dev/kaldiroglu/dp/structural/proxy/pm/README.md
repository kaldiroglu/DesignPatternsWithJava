<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `proxy.pm` — the Prime Minister, in three stages

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

A citizen wants to tell the Prime Minister about a problem. Three packages, and only the
third is the pattern — which is the point of having all three.

| | What it does | What is wrong with it |
|---|---|---|
| `pm1` | The citizen holds the PM directly; the PM screens his own calls | The screening rule lives **inside the class it exists to protect**, and every citizen has his direct number |
| `pm2` | A `Proxy` class screens, and the PM does one job | `Proxy` and `PM` **share no type**, so `Citizen`'s field had to change — every client was edited to accept the stand-in |
| `pm3` | A `PM` interface, `RealPM`, `ProxyPM`, and a `PMSecretary` that hands out the proxy | Nothing. This is the pattern |

## The move that matters

Stage 2 looks like a proxy and is not one. Read `pm2.Citizen`:

```java
private final Proxy proxy;      // <-- was PM in pm1
```

**A stand-in the client can see is not a proxy.** In `pm3` the field is a `PM` again,
exactly as it was in stage 1 — the screening happens and the citizen does not know it does.

## Two proxy kinds in one object

`ProxyPM` is a **protection proxy** (GoF, p. 208): it decides whether the real subject is
called at all, and `findJob` is answered without ever forwarding. No decorator would do
that — a decorator adds to what the subject does; this one may replace it entirely.

`PMSecretary` makes it a **virtual proxy** as well: the Prime Minister is not created until
somebody actually asks for him.

## Run it with

```bash
mvn -o test -Dtest=PrimeMinisterTest      # 7 tests

java -cp target/classes dev.kaldiroglu.dp.structural.proxy.pm.pm1.Main
java -cp target/classes dev.kaldiroglu.dp.structural.proxy.pm.pm2.Main
java -cp target/classes dev.kaldiroglu.dp.structural.proxy.pm.pm3.Main
```
