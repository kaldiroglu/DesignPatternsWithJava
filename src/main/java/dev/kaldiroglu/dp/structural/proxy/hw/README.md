<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `proxy.hw` — worked solutions to the homework

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

The repository already shows a **virtual** proxy (`proxy.gof`) and two **protection**
proxies (`proxy.pm`, `proxy.network`). These three cover the kinds it does not, so that
between them all four of GoF's named kinds appear, plus the one they never named.

**Try them before you read them.** Each package has a runnable `Main` and a test class.

| Package | Kind | What the code settles |
|---|---|---|
| `pricing` | caching *(not named by GoF)* | five requests cost **one** supplier call — and invalidation is the hard half |
| `remote` | remote | a six-line basket becomes **6 round trips, 720 ms** |
| `vault` | smart reference | two holders, two closes, **one** real close |

## 1 · `pricing`

The saving is easy to demonstrate and easy to believe. The exercise is the other half: for up
to one time-to-live this proxy returns a price that is **wrong, and confidently**. No pattern
fixes that; somebody has to decide how stale the business can afford to be.

Time is injected, so expiry is tested without any test sleeping.

## 2 · `remote`

GoF's first named kind: "a local representative for an object in a different address space."

The interface says nothing about latency, failure or timeouts, and **that silence is the
exercise**. A loop that was free against a local object costs a round trip per iteration; the
proxy did its job perfectly by making the remote object look local, and that is exactly how
the mistake gets made. Failures are scripted and latency is counted rather than slept, so
every run agrees.

## 3 · `vault`

GoF's fourth kind, and they list three duties for it — this one does all three: **counting
references** so the document closes only when the last holder lets go, **loading on first
access** (a virtual proxy hiding inside a smart reference), and **locking** so a second writer
is refused rather than silently overwriting the first.

The lesson worth taking: `close()` on the proxy does not mean *close*. It means *I am
finished with it*.

## Run it with

```bash
mvn -o test -Dtest='CachingProxyTest,RemoteProxyTest,SmartReferenceTest'    # 19 tests

java -cp target/classes dev.kaldiroglu.dp.structural.proxy.hw.pricing.Main
java -cp target/classes dev.kaldiroglu.dp.structural.proxy.hw.remote.Main
java -cp target/classes dev.kaldiroglu.dp.structural.proxy.hw.vault.Main
```
