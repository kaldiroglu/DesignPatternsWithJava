# Iterator — GoF's own example

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

Design Patterns, pp. 257–271. A list should let clients walk its elements without showing
how it stores them, and more than one walk may be needed at the same time.

## Before the pattern — `problem.CursorList`

The list walks itself: `first`, `next`, `isDone` and `currentItem` are on the list, and so
is the cursor they move. A loop inside a loop moves the same cursor. `Main` pairs three
employees with each other: with one cursor it makes 3 pairs instead of 9.

## After it — `solution`

| Class | GoF participant | What it does |
|---|---|---|
| `Iterator` | Iterator | GoF's four operations: `first`, `next`, `isDone`, `currentItem` |
| `AbstractList` | Aggregate | `createIterator()` — a factory method |
| `List` | ConcreteAggregate | Stored in an array |
| `ChainList` | ConcreteAggregate | Stored as linked nodes (GoF use a skip list) |
| `ListIterator`, `ReverseListIterator` | ConcreteIterator | Front to back, and back to front, over `List` |
| `ChainListIterator` | ConcreteIterator | Follows the links of `ChainList` |
| `PrintEmployees` | Client | Works with any `Iterator<Employee>` |
| `ListTraverser`, `PrintNEmployees` | Internal iterator | The traverser runs the loop; `processItem` returns `false` to stop |

`java.util.Iterator` does the same job with two operations: `hasNext()` is `!isDone()`, and
`next()` is `currentItem()` followed by `next()`.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.iterator.gof.Main
```
