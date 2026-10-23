# State — an online order

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The main worked example of the State deck. An order is placed, paid, shipped, delivered or
cancelled, and what it may do depends on which. A courier gets three delivery attempts; after
three failures the parcel goes back to the warehouse and can be shipped again.

## Three attempts — `problem`

| Stage | Classes | What it gets right | What it costs |
|---|---|---|---|
| one | `FlagOrder` | Simple | Four flags make sixteen combinations; five are real |
| two | `SwitchingOrder`, `Status` | Exactly one status; switches without `default` | Each status's rules are spread over every method |
| three | `EnumOrder`, `OrderStatus` | Each status's rules in one constant | A constant cannot hold one order's data |

**Where stage three fails.** The failed-attempt count has to live in the order. Shipping
again does not reset it, so the second shipment's first failure is attempt 4. The check is
`== 3`, so the parcel never goes back again: the courier has no limit.

Each slide has its own runnable class, so a run prints only what that slide shows:

| Class | Slide | What it prints |
|---|---|---|
| `StageOneMain` | stage one | A flags order paid, shipped and delivered |
| `StageTwoMain` | stage two | The switch refuses to cancel a shipped order |
| `StageThreeMain` | stage three | Three failures, and the parcel back at the warehouse |
| `SecondShipmentMain` | the problem with stage three | The second shipment's failures counted as 4, 5 and 6 |

`SampleOrder` builds the order after its first shipment, which the last two classes share.

## The pattern — `solution`

| Class | Role | What it does |
|---|---|---|
| `Order` | Context | Forwards each request to its state and keeps the state it gets back |
| `OrderState` | State | A `sealed` interface; every operation is refused by default |
| `Placed`, `Paid`, `Shipped`, `Delivered`, `Cancelled` | ConcreteState | Records. `Shipped` holds its tracking number and attempts. |
| `SecondShipmentMain` | — | Runs the same story with stage three and with State objects: the second shipment's first failure is attempt 4, then attempt 1 |
| `RefusedRequestMain` | — | Asks a shipped order to cancel, and the state refuses |

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile

# the three attempts, one slide at a time
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.order.problem.StageOneMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.order.problem.StageTwoMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.order.problem.StageThreeMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.order.problem.SecondShipmentMain

# the pattern
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.order.solution.RefusedRequestMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.order.solution.SecondShipmentMain

# the tests of this example
mvn -o -q test -Dtest='dev.kaldiroglu.dp.behavioral.state.order.**'
```
