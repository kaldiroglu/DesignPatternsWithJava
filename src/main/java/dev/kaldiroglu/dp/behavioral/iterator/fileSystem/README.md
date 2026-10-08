# Iterator — the file system

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-10-08*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The author's own example. A `Directory` holds files, shortcuts, aliases and other
directories, and gives out a `DirectoryIterator` to list them.

| Class | Participant |
|---|---|
| `Storage`, `StorageElement` | the element type |
| `File`, `ShortCut`, `Alias` | elements |
| `Directory` | Aggregate — `iterator()` creates the iterator |
| `iterator.DirectoryIterator` | ConcreteIterator — implements `java.util.Iterator` |
| `Test` | Client, with a `main` method |

Three things worth knowing, all used in the deck's exercises:

- `DirectoryIterator` walks only the directory's own elements. A folder inside it is one
  element; its contents are not visited. The Composite deck's
  `structural.composite.fileSystem.iterator.DirectoryIterator` walks the whole tree.
- `Directory.elements()` returns the internal list. While that method is public, a caller
  can go around the iterator and change the directory.
- In `DirectoryIterator<Storage>`, `Storage` is a type parameter, not the `Storage`
  interface. The name hides the interface inside the class.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.iterator.fileSystem.Test
```
