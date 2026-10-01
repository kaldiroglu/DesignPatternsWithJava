# Command — the teller's Undo button

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-02*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The main worked example of the Command deck. A bank's teller screen gets an Undo button,
the auditors want the day's journal, and the account should go on doing banking and
nothing else.

## The shared types — `domain`

`Money` (a value, two decimal places), `Account` (owner, balance, deposit, withdraw) and
`InsufficientFundsException`. `Account` is the **Receiver**, and it never learns that it
can be undone.

## Three honest attempts — `problem`

| Stage | Class | What it gets right | What it costs |
|---|---|---|---|
| one | `OneStepAccount` | One undo, in a few lines | One step only; two of its fields are bookkeeping |
| two | `HistoryAccount` | Undo, redo and a journal | Eight public operations, three of them banking; each operation lives in three places |
| three | `Teller` + `domain.Account` | The account is clean again | The teller branches on a `Kind` to undo and to redo |

**The reversal.** At stage three the bank adds transfers, and `Teller.transfer` reuses
`withdraw` and `deposit` — exactly what a good developer should do. It records two
entries. The teller presses Undo once and gets back the deposit only: 300 lira leave
Deniz's account and arrive nowhere. The design recorded the parts of a request and lost the
whole, because the request was never a thing it could hold.

## The pattern — `solution`

| Class | Role | What it does |
|---|---|---|
| `Transaction` | Command | `execute()`, `undo()`, `description()` — no arguments, because the request is complete when it is made |
| `Deposit`, `Withdraw` | ConcreteCommand | Bind an account to an action, and know their own opposite |
| `CloseOut` | ConcreteCommand | Pays out the whole balance; remembers what it took, because the request had no amount |
| `Transfer` | ConcreteCommand (a MacroCommand) | A withdrawal and a deposit, executed all or nothing, undone together in reverse |
| `Teller` | Invoker | Performs, keeps, undoes and redoes; names no operation |
| `StandingOrders` | — | Requests made in the morning and run at night: the "queue requests" half of the intent |

## The author's earlier versions — `account1`, `account2`

Kept as they were. `account1` is the version in the author's original slides, with a
`Transaction` interface carrying `execute`, `undo` and `redo` and two factories.
`account2` is a later variation in which the account accepts a transaction —
`Account.changeBalance(Transaction)` — so the receiver runs the command it is handed.

## Run it with

```bash
cd "~/Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
mvn -o test -Dtest='dev.kaldiroglu.dp.behavioral.command.account.*Test'
```
