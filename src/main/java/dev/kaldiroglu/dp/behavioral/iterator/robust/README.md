# Iterator — what happens when the collection changes during a walk

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-23*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

GoF implementation issue 3 (how robust is the iterator?): an iterator must decide what
happens if the collection changes while it walks. It can fail, ignore the change, or see
it, and the caller must know which. The standard library uses all three answers, and the
collection decides which one applies.

Each walk reads A, B and C, and the collection changes after A.

| What the iterator does | Collection | What happens |
|---|---|---|
| Fails | `ArrayList`, `HashMap` | The next step throws `ConcurrentModificationException` |
| Ignores the change | `CopyOnWriteArrayList` | It walks a snapshot taken when the walk started: A, B, C |
| May see the change, and never fails | `ConcurrentHashMap` | "Weakly consistent": no exception; a new key may or may not be seen |
| Replacing an element | `ArrayList.set` | Not counted as a change: no exception, and the walk sees A, B, Z |

**How "fails" works.** The collection counts its structural changes in `modCount`. Each
iterator remembers the count it started with and compares the two on every step.

**How to change a list during a walk.** Remove through the iterator, with
`Iterator.remove()`, or walk a copy and change the original.

| Class | What it does |
|---|---|
| `Walk` | Walks a collection, makes one change after the first element, and reports what the iterator did |
| `Main` | Runs the five cases above |

There is no `uml/` diagram: the example has no classes of its own to draw, only the
standard library's.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.iterator.robust.Main

# the tests of this example
mvn -o -q test -Dtest='dev.kaldiroglu.dp.behavioral.iterator.robust.**'
```
