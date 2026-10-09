# Iterator — reaching everyone in a department

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The main worked example of the Iterator deck. A company is a tree: a department has
members and sub-departments. Payroll, the phone book and HR all need "everyone in this
department", and the department should not show how it stores people.

## Three attempts — `problem`

| Stage | Class | What it gets right | What it costs |
|---|---|---|---|
| one | `OpenDepartment` + `PayrollRun` | Simple | It gives out its internal lists. Every caller writes the same recursion and can change the lists. |
| two | `CopyingDepartment` | One recursion; callers get a copy | It copies the whole department on every call, in one order only. |
| three | `CallbackDepartment` | No copy; two orders | The department runs the walk. A caller cannot walk two departments side by side. |

**Where stage three fails.** After a reorganization HR asks: "What changed?" The report
must walk the old chart and the new chart together and stop at the first difference. With
callbacks it cannot, so `problem.ChangeReport` copies both departments into lists first.
GoF implementation issue 1 (who controls the iteration?) makes the same point: comparing
two collections is easy with an external iterator and "practically impossible" with an
internal one.

## The pattern — `solution`

| Class | Role | What it does |
|---|---|---|
| `Department` | Aggregate | Implements `Iterable<Employee>`; `iterator()` and `byLevel()` create iterators. No getter for its lists. |
| `DepthFirstIterator` | ConcreteIterator | Department by department, with a stack |
| `LevelOrderIterator` | ConcreteIterator | Level by level, with a queue |
| `ChangeReport` | Client | Moves two iterators forward together. `firstDifference` stops at the first difference; `allDifferences` walks both charts to the end |
| `Main` | — | Builds a small company and runs both orders. Then it compares it with a reorganized copy in which three people are new: the first difference is the head of sales, and all differences lists the three |

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.iterator.orgchart.solution.Main
```
