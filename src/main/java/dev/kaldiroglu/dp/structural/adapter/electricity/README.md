<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `adapter.electricity` — a Turkish appliance on an American socket

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

Two worlds that were designed without each other in mind:

| | Turkish | American |
|---|---|---|
| Power | `providePowerAt220V()` | `providePowerAt110V()` |
| Switching | `turnOn()` **and** `turnOff()` | one `pushSwitch()` that **toggles** |

The voltage is the obvious mismatch. The switching is the interesting one — **two operations
against one toggle** — and it is where most of the lessons in this package live.

## The layout

| Package | What it shows |
|---|---|
| `domain/tr`, `domain/us` | The two worlds, unchanged and unaware of each other |
| `problem1` | Teach the appliance about both sockets — and lose the interface |
| `problem2` | Move the branching to a subclass whose name states two things |
| `powerAdapter1` | **Object adapter**, stateful, and correct |
| `powerAdapter2` | **Object adapter**, and one that does more than translate |
| `classAdapter` | **Class adapter** — extends the adaptee, implements the target |
| `twoWayAdapter` | GoF implementation issue 4: an adapter that is both interfaces |
| `pluggable/…/abstractOperations` | Pluggable adapter, GoF technique (a) |
| `pluggable/…/delegateObject` | Pluggable adapter, GoF technique (b) |
| `pluggable/…/parameterized` | Pluggable adapter, GoF technique (c) |

## The two problem attempts

`problem1` gives `TurkishHomeAppliance` a second field, a boolean flag, and a branch in every
method. It works. What it costs is on the first line of its demo:

```java
// Can't have a reference of type Appliance anymore
TurkishHomeAppliance turkishMixer = new TurkishHomeAppliance("Mixer");
```

`setUSPowerSource` is not on the `Appliance` interface, so the client has to name the
concrete class. **Polymorphism was the reason the interface existed, and this design spends
it.**

`problem2` moves the same branching into a subclass called
`TurkishHomeApplianceCompatibleWithUSPowerSource`. A class name that states two things is the
reliable sign of one class carrying two axes — a third socket standard costs a third class,
and a fourth appliance kind costs all of them again.

## The two object adapters

Both translate the Turkish two-operation interface onto the American single toggle, and both
**remember whether the power is on** — they have to. `pushSwitch` toggles, so mapping
`turnOn`/`turnOff` onto it is only correct if somebody tracks the state; without that, two
consecutive `turnOn` calls would leave the appliance **off**.

> Adapting an interface is not only renaming methods. It is preserving the **meaning** across
> a different operation model.

`powerAdapter2` additionally does work of its own — a safety check and voltage regulation,
performed once when the adapter is built. That is GoF's point that an adapter need not confine
itself to translation.

## Class against object

`classAdapter.USTurkishPowerAdapter` extends the adaptee and implements the target, so it has
**no adaptee field at all** and reaches `pushSwitch` by inheritance. It is therefore also
usable *as* a `USPowerSource` — something the object adapter cannot offer. The price is Java's
single inheritance: one adaptee, chosen at compile time, forever.

## Run it with

```bash
mvn -o test -Dtest=ElectricityAdapterTest       # 9 tests

java -cp target/classes dev.kaldiroglu.dp.structural.adapter.electricity.problem1.Main
java -cp target/classes dev.kaldiroglu.dp.structural.adapter.electricity.powerAdapter1.Main
java -cp target/classes dev.kaldiroglu.dp.structural.adapter.electricity.classAdapter.Main
java -cp target/classes dev.kaldiroglu.dp.structural.adapter.electricity.twoWayAdapter.Main
java -cp target/classes dev.kaldiroglu.dp.structural.adapter.electricity.pluggable.electricity.parameterized.Main
```
