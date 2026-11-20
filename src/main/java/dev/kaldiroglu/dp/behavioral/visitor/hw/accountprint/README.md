# Visitor — homework 1: print accounts to an output that is passed in

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-11-20*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The homework asks: a bank has checking, savings and loan accounts. Print them all to an
output that is passed in. Who owns the output?

The printer visitor owns it. Each printer is created with a `PrintWriter` and writes one
line per account. The account classes do not print, and a new format is a new visitor.

| Class | Role |
|---|---|
| `Account` | Element: `owner()` and `accept(visitor)` |
| `CheckingAccount`, `SavingsAccount`, `LoanAccount` | Concrete elements, as records |
| `AccountVisitor` | Visitor: one `visit` for each kind of account |
| `TextPrinter`, `HtmlPrinter` | Concrete visitors, each given its `PrintWriter` |
| `Main` | Prints three accounts as text, then as HTML |

## Diagram

![Visitor - Account Printing (Homework) - Class Diagram](uml/Visitor%20-%20Account%20Printing%20(Homework)%20-%20Class%20Diagram.png)

## Tests

`AccountPrintTest` checks the behavior above and the exact output of `Main`. It is a unit test.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest=AccountPrintTest
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.visitor.hw.accountprint.Main
```
