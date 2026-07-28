<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `proxy` — the Proxy pattern

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

**Source:** *Design Patterns: Elements of Reusable Object-Oriented Software* — Proxy,
pp. 207–217.

> Provide a surrogate or placeholder for another object to control access to it.

| Package | Kind of proxy | What it shows |
|---|---|---|
| `gof` | virtual | The book's own example — a document laid out from twenty images, none of them read from disk |
| `pm` | protection, virtual | Three stages: no proxy, a stand-in with no shared type, then the pattern |
| `network` | protection | A firewall, from a 2009 seminar |
| `hw` | caching, remote, smart reference | Worked homework solutions |

Between them, all four kinds GoF name — remote, virtual, protection, smart reference — and
caching, which they do not.

## The sentence worth keeping

Proxy and Decorator have the same class diagram. The difference is what the forwarding is
*for*:

- a **decorator** forwards and **adds** to the answer;
- a **proxy** decides **whether the subject is called at all**.

`ProxyPM.findJob` and `CachingPriceProxy.priceOf` both answer without forwarding. No
decorator does that.

## Run it with

```bash
mvn -o test -Dtest='PrimeMinisterTest,NetworkProxyTest,ImageProxyTest,CachingProxyTest,RemoteProxyTest,SmartReferenceTest'
python3 tools/make_package_diagrams.py
```
