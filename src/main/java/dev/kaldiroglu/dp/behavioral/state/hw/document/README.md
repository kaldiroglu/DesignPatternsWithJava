# State — homework 2: a document workflow in a table

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-11-20*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The homework asks: a document is a draft, in review, published or archived. Should each
status be a class?

No. The statuses have no behavior of their own, so a central table from status and action
to the next status is enough. This is GoF implementation issue 2 (a table-based
alternative). A new rule is one line in `Workflow`. The cost: a status that needs its own
behavior or data does not fit a table.

| Class | Role |
|---|---|
| `Document` | Context: asks the workflow for its next status, and refuses what the table does not allow |
| `Workflow` | The table: four allowed transitions |
| `Status` | The four statuses, as names only |
| `Action` | Submit, approve, reject, archive |
| `Main` | Refuses an approval of a draft, then takes a document to archived |

## Diagram

![State - Document Workflow (Homework) - Class Diagram](uml/State%20-%20Document%20Workflow%20(Homework)%20-%20Class%20Diagram.png)

## Tests

`DocumentTest` checks the behavior above and the exact output of `Main`. It is a unit test.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest=DocumentTest
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.state.hw.document.Main
```
