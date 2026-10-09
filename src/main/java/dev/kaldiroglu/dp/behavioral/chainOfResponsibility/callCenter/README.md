# Chain of Responsibility — a call center

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Customers call a call center. Every call reaches the standard desk first. A desk answers
the customers it serves and passes the others to the next desk: standard, then gold, then
VIP.

| Class | Role |
|---|---|
| `CallTaker`, `AbstractCallTaker` | Handler — `answer(Customer)`, and the next desk |
| `StandardCallTaker`, `GoldCallTaker`, `VipCallTaker` | ConcreteHandler |
| `StandardCustomer`, `GoldCustomer`, `VipCustomer` | The request |
| `Test` | Client — builds the chain and creates random customers |

`Test` picks customers with `Math.random()`, so the output changes from run to run.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter.Test
```
