<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `proxy.pm` — the Prime Minister, in four stages

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

A citizen wants to tell the Prime Minister about a problem. Four packages. `pm1` and `pm2` are
not the pattern, `pm3` is, and `pm4` removes what `pm3` still gets wrong.

| | What it does | What is wrong with it |
|---|---|---|
| `pm1` | The citizen holds the PM directly; the PM screens her own calls | The screening rule lives **inside the class it exists to protect**, and every citizen holds the Prime Minister's own number |
| `pm2` | A `Proxy` class screens, and the PM does one job | `Proxy` and `PM` **share no type**, so `Citizen`'s field had to change — every client was edited to accept the stand-in |
| `pm3` | A `PM` interface, `RealPM`, `ProxyPM`, and a `PMSecretary` that hands out the proxy | `Citizen` takes a `PMSecretary`, so the client changed. `ProxyPM` receives a `RealPM` that is already built, so the proxy delays nothing. `RealPM` is public |
| `pm4` | `ProxyPM` creates a package-private `RealPM` on the first forwarded call, and `Citizen` takes a `PM` | Nothing. A refused call and `findJob` never create the Prime Minister |

## The move that matters

Stage 2 looks like a proxy and is not one. Read `pm2.Citizen`:

```java
private final Proxy proxy;      // <-- was PM in pm1
```

**A stand-in the client can see is not a proxy.** In `pm3` the field is `PM`, an interface.
In stage 1 `PM` was a class with the same name. The screening happens, and the citizen does
not know it does.

## Two proxy kinds in one object

`ProxyPM` is a **protection proxy** (GoF, p. 208): it decides whether the real subject is
called at all, and `findJob` is answered without ever forwarding. No decorator would do
that — a decorator adds to what the subject does; this one may replace it entirely.

In `pm3`, `PMSecretary` only delays creating the pair of objects, and it does that when a
citizen is constructed. That is a lazy factory and not a virtual proxy. In `pm4`, `ProxyPM` is
a **virtual proxy** too: it starts with no `RealPM` and creates one on the first call it
forwards.

## Run it with

```bash
mvn -o test -Dtest=PrimeMinisterTest      # 10 tests

java -cp target/classes dev.kaldiroglu.dp.structural.proxy.pm.pm1.Main
java -cp target/classes dev.kaldiroglu.dp.structural.proxy.pm.pm2.Main
java -cp target/classes dev.kaldiroglu.dp.structural.proxy.pm.pm3.Main
java -cp target/classes dev.kaldiroglu.dp.structural.proxy.pm.pm4.Main
```
