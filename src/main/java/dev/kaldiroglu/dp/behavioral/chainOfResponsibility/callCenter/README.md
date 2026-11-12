# Chain of Responsibility — a call center

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Customers call a call center. Every call reaches the standard desk first. A desk answers
the customers it serves and passes the others to the next desk: standard, then gold, then
VIP. A higher desk also serves lower customers: the gold desk serves gold and standard
customers, and the VIP desk serves VIP, gold and standard customers. So more than one desk
may answer a call, and each customer is answered by the first desk in the chain that serves
them. The order of the desks decides which desk that is.

| Class | Role |
|---|---|
| `CallTaker`, `AbstractCallTaker` | Handler — `answer(Customer)`, and the next desk |
| `StandardCallTaker`, `GoldCallTaker`, `VipCallTaker` | ConcreteHandler |
| `StandardCustomer`, `GoldCustomer`, `VipCustomer` | The request |
| `Main` | Client — builds the chain standard, gold, VIP and sends one customer of each kind |
| `VipFirstMain` | The same customers with the VIP desk first: it answers all three |

`Main` and `VipFirstMain` send the same three customers, so their output is the same on every
run. `CallCenterTest` checks it line by line.

## Diagrams

`uml/` holds the class diagram, and a sequence diagram of `Main`'s three customers: the
standard desk answers the standard customer, passes the gold customer to the gold desk, and
the VIP customer goes on through the gold desk to the VIP desk.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter.Main
java -cp target/classes dev.kaldiroglu.dp.behavioral.chainOfResponsibility.callCenter.VipFirstMain
mvn -o -q test -Dtest=CallCenterTest
```
