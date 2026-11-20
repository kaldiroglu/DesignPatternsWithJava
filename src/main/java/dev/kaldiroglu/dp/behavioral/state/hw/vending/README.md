# State — homework 3: a vending machine

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-11-20*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The homework asks: a machine waits for a coin, has a coin, or is sold out. What happens to
a coin put into a sold-out machine?

It is returned at once: the sold-out state never takes it. A second coin is returned too,
and a refill makes a sold-out machine wait for a coin again.

| Class | Role |
|---|---|
| `VendingMachine` | Context: the stock and the current state |
| `VendingState` | State: a sealed interface; `refilled` does nothing by default |
| `WaitingForCoin`, `HasCoin`, `SoldOut` | Concrete states |
| `Main` | Sells the only drink, returns the next coin, refills |

## Diagram

![State - Vending Machine (Homework) - Class Diagram](uml/State%20-%20Vending%20Machine%20(Homework)%20-%20Class%20Diagram.png)

## Tests

`VendingMachineTest` checks the behavior above and the exact output of `Main`. It is a unit test.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest=VendingMachineTest
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.hw.vending.Main
```
