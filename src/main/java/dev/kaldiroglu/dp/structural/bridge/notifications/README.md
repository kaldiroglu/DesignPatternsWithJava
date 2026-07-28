<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `bridge.notifications` — two axes that grow for unrelated reasons

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

An order system has to tell people things. **What** it says (simple, urgent, digest) is
driven by the product team; **how** it goes out (email, SMS, push) is driven by users,
contracts and vendors. The two lists have nothing to do with each other — and that is the
entire problem.

## Layout

```
notifications/
├── uml/
│   ├── problem/               Bridge - Notifications (Problem) - Class Diagram
│   └── solution/              Bridge - Notifications (Solution) - Class Diagram
│                              Bridge - Notifications (Solution) - Object Diagram
│                              Bridge - Notifications (Solution) - Sequence Diagram
│                              Bridge - Notifications (Variations) - Class Diagram
├── domain/                    Recipient, Message, DeliveryResult, TransportLog,
│                              Transports — the three vendor SDKs, as given
├── problem/                   SwitchingNotifier, the class-per-pair set,
│                              EmailSender + EmailBoundUrgentNotification, and Main
└── solution/
    ├── classic/               Notification (+3 refinements),
    │                          NotificationChannel (+3 implementations), and Main
    ├── factory/               ChannelFactory, PreferenceChannelFactory,
    │                          NotificationService, and Main
    └── shared/                PooledChannel, and Main
```

## The measuring instrument

Every design here — naive and otherwise — ends at the same three vendor calls in
`Transports`, and every call is recorded by `TransportLog`. So the designs are doing
identical work on identical infrastructure, and each claim below is a count rather than an
opinion. Failures are *scripted* (`failNext(n)`), never random.

## The three naive designs (`problem`)

| Design | It works, but |
|---|---|
| `SwitchingNotifier` | 3 × 3 = **nine branches** in one class. No edit touches one axis alone. The 160-character SMS rule appears in four places — and `sendDigest` forgot it, so an SMS digest **throws**. |
| Class per pair | Nine classes, whose names have to state both axes. The retry loop — the only thing "urgent" means — is written once per channel. |
| `EmailBoundUrgentNotification extends EmailSender` | The notification **is** an email sender, so Bora's stored preference for SMS is silently ignored and the result still says "delivered". |

## The bridge (`solution`)

| | Naive (class per pair) | Bridge |
|---|---|---|
| 3 kinds × 3 channels | 9 classes | **6 classes** |
| A fourth channel | +3 | **+1**, no kind touched |
| A fourth kind | +3 | **+1**, no channel touched |
| Choose the channel at run time | impossible | `setChannel(...)`, or a factory |

The split to point at: **the implementor answers questions about the channel**
(`addressOf`, `maxBodyLength`, `supportsSubject`, `deliver`) and knows nothing about
notifications. The abstraction composes those primitives and never asks which channel it is
holding — there is no `instanceof` anywhere in the solution package.

One long message, one `UrgentNotification` class, three channels: **218 characters over
email, 160 over SMS, 120 over push.**

## Variations

| Variation | GoF issue | What it answers |
|---|---|---|
| `solution/factory` | Implementation issue 2 (p. 155) | *Who* chooses the implementor — here, the recipient's stored preference, at run time |
| `solution/shared` | Implementation issue 3 (p. 156) | One channel object serving many notifications, and what that costs in shared state |

## What the tests establish — 27 tests

| Test | Count | Point |
|---|---|---|

## Run it with

```bash
mvn -o test -Dtest='ProblemTest,ClassicBridgeTest,VariationsTest,DesignComparisonTest'

java -cp target/classes dev.kaldiroglu.dp.structural.bridge.notifications.solution.classic.Main
java -cp target/classes dev.kaldiroglu.dp.structural.bridge.notifications.problem.Main
java -cp target/classes dev.kaldiroglu.dp.structural.bridge.notifications.solution.factory.Main
java -cp target/classes dev.kaldiroglu.dp.structural.bridge.notifications.solution.shared.Main

find uml -name "*.puml" -exec plantuml -tpng {} \;
```
