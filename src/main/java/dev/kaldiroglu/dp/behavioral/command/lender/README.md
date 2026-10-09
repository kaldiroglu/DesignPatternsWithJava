# Command — the lender

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-02*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

An example in three steps. Somebody lends money and should not know who
takes it — nor what is done with it.

| Package | What the lender knows | Pattern |
|---|---|---|
| `problem1` | The class `Borrower` and its method `borrow` | none |
| `problem2` | An interface `Borrower` and its method `borrow` | Strategy: the object is hidden, the method is not |
| `pattern` | An interface `Command` and its method `execute` | Command: the object and the action are both hidden |

In `pattern`, `TaxOffice` takes the money for a tax debt. The lender calls the same
`execute(money)` and cannot tell that nothing was borrowed.

**What it leaves out.** The amount arrives at `execute` time rather than being bound when
the command is made, so this command cannot be queued or undone without storing the amount
somewhere else. It has one abstract method, so a lambda can stand in for it:
`lender.lend(money -> ..., 2000)`. Compare `account.solution.Transaction`, whose `execute()`
takes nothing.

## The lambda version — `lambda`

`pattern.Command` has one method, `execute(int)`, so it is a function from an amount to
nothing, which the JDK already has as `IntConsumer`. In `lambda`, `Lender.lend` takes an
`IntConsumer`, and `Main` lends to two lambdas: a borrower and a tax office. There is no
`Command` interface and no command class, and the output is the same as `pattern.Main`.
This is the form most Java code uses for a command with no undo.

## Tests

`src/test/java/dev/kaldiroglu/dp/behavioral/command/lender` holds one test class per step:
`Problem1Test`, `Problem2Test`, `PatternTest` and `LambdaTest`, 15 tests in all. They check what the
lender's `lend` method takes (a class, an interface, a `Command`), that a lambda can be a
borrower or a command, that `TaxOffice` does not use the amount, and the output of each
`Main`. `Printed` captures standard output for them.

## Run it with

```bash
cd "~/Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.lender.problem1.Main
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.lender.problem2.Main
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.lender.pattern.Main
java -cp target/classes dev.kaldiroglu.dp.behavioral.command.lender.lambda.Main

# the tests
mvn -o -q test -Dtest='Problem1Test,Problem2Test,PatternTest,LambdaTest'
```
