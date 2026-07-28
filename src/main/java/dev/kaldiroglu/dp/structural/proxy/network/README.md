<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `proxy.network` — a firewall

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

Written for a Design Patterns seminar in **May 2009**, and kept because it makes the same
point as `proxy.pm` in a domain with no people in it: the pattern is about controlling
access, not about politeness.

`ProxyServer` implements `Network` and holds a `Network`. Every request is logged, then
checked, then forwarded — and a refused one never reaches the `Gateway` at all.

## What the tests establish

The rule lives in the proxy and **only** in the proxy. `theRealSubjectKnowsNothing` calls
the gateway directly with the forbidden request and it succeeds — which is the proof.
`refusedRequestsAreLogged` shows the proxy saw a request it would not pass on.

## What was changed

- `YasakKardesimException` → **`ForbiddenAccessException`**, and the log messages are now
  English. This repository keeps all identifiers and output in English.
- `Logger` printed `new Date()`, so no two runs agreed and nothing could be asserted. It now
  records entries in a list as well as printing them.
- `NetworkClient` → `Main`, matching the rest of the repository.
- `Gateway` counts its calls, so "never reached the gateway" is a number rather than a claim.

## Run it with

```bash
mvn -o test -Dtest=NetworkProxyTest       # 7 tests
java -cp target/classes dev.kaldiroglu.dp.structural.proxy.network.Main
```
