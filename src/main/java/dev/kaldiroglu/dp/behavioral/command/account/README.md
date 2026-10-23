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

| Run | What it prints | Slide |
|---|---|---|
| `problem.StageOneMain` | Two undos leave 1500.00: the second undo did nothing | stage one |
| `problem.StageTwoMain` | Both steps undone, and the journal | stage two |
| `problem.TransferUndoMain` | One undo after a transfer: Deniz 700.00, Emre 0.00 | the problem with stage three |

## The pattern — `solution`

| Class | Role | What it does |
|---|---|---|
| `Transaction` | Command | `execute()`, `undo()`, `description()` — no arguments, because the request is complete when it is made |
| `Deposit`, `Withdraw` | ConcreteCommand | Bind an account to an action, and know their own opposite |
| `CloseOut` | ConcreteCommand | Pays out the whole balance; remembers what it took, because the request had no amount |
| `Transfer` | ConcreteCommand (a MacroCommand) | A withdrawal and a deposit, executed all or nothing, undone together in reverse |
| `Teller` | Invoker | Performs, keeps, undoes and redoes; names no operation |
| `StandingOrders` | — | Requests made in the morning and run at night: the "queue requests" half of the intent |

| Run | What it prints | Slide |
|---|---|---|
| `solution.TransferMain` | One undo takes back the whole transfer: Deniz 1000.00, Emre 0.00 | the change in one slide; several steps as one |
| `solution.CloseOutMain` | Close out 750.00, undo, and 750.00 come back | a command remembers what it did |
| `solution.StandingOrdersMain` | Three orders wait until the night run; the last one is undone next morning | queue requests |

## The lambda version — `lambda`

`Transaction` has three methods — `execute`, `undo`, `description` — so one lambda cannot
implement it. `LambdaTransaction` is a record that holds one function for each, and
`Transactions` builds the four transactions from lambdas. The `Teller` and the
`StandingOrders` are the ones from `solution`; they cannot tell the difference.
`lambda.TransferMain`, `lambda.CloseOutMain` and `lambda.StandingOrdersMain` print the same
lines as the classes of the same name in `solution`.

- A deposit, a withdrawal and a transfer are easy: each undo is the opposite operation.
- A close-out is not. It must remember how much it took, to give it back on undo, and a
  lambda cannot have a field. Its lambdas share a one-element array, `Money[] taken` — a
  small class written by hand. That is why `solution.CloseOut` is a class: a request that
  must be undone needs to remember something.
- The description is a `Supplier<String>`, because a close-out knows what it paid only
  after it has run.

`LambdaTransactionTest` checks that the output and every journal line are the same as with
the classes, that the close-out gives back 750.00, and that a failed transfer changes
nothing.

## Run it with

```bash
cd "~/Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile

# the three attempts
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.account.problem.StageOneMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.account.problem.StageTwoMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.account.problem.TransferUndoMain

# the pattern
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.account.solution.TransferMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.account.solution.CloseOutMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.account.solution.StandingOrdersMain

# the same with lambdas
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.account.lambda.TransferMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.account.lambda.CloseOutMain
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.account.lambda.StandingOrdersMain

# the tests of this example
mvn -o test -Dtest='dev.kaldiroglu.dp.behavioral.command.account.**.*Test'
```
