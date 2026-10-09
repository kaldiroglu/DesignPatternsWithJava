# Chain of Responsibility — who approves an expense

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-09*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The main worked example of the Chain of Responsibility deck. A company's expense rule:
every expense is approved by someone with enough authority, and never by the person who
spent it. Elif leads the team (up to 1,000), Burak manages (10,000), Cem directs (50,000),
and Deniz is the CFO (200,000).

## Three attempts — `problem`

| Stage | Class | What it gets right | What it costs |
|---|---|---|---|
| one | `ApprovalService` | Correct | Every name, limit and rule in one method |
| two | `TeamLead`, `Manager`, `Director`, `Cfo` | Each rule is with its approver | Each approver creates the next one by class; the order is fixed in code |
| three | `LimitTable` | No names in code; a level is one more row | It chooses by amount alone |

**Where stage three fails.** Burak spends 5,000 on a conference, and the table sends it to
Burak, who approves his own expense. Cem's 30,000 goes to Cem. Stages one and two send them
to Cem and Deniz.

## The pattern — `solution`

| Class | Role | What it does |
|---|---|---|
| `ExpenseHandler` | Handler | Knows the next link; asks itself first, then passes the expense on |
| `Approver` | ConcreteHandler | Approves if the amount is within its limit and the expense is not its own |
| `AuditLog` | ConcreteHandler | Records every expense and always passes it on |
| `Main` | Client | Runs six expenses through every design |

An expense of 500,000 reaches the end of the chain, and the answer is "no one may approve
it".

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.chainOfResponsibility.expense.solution.Main
```
