# Visitor — homework 2: who walks a folder tree?

*Claude Opus 5.5 (claude-opus-5-5) — Created on 2026-11-20*

For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev

The homework asks: add up file sizes and list a folder tree with indentation. Who walks the
tree?

The tree walks itself. `Folder.accept` visits the folder, then passes the visitor to each
child, so no visitor has a loop. This is the first answer of GoF implementation issue 2
(who is responsible for traversing the object structure?). A folder is visited twice,
on enter and on leave, so the listing can keep track of the depth.

| Class | Role |
|---|---|
| `Entry` | Element: `name()` and `accept(visitor)` |
| `FileEntry` | A file with a size |
| `Folder` | A folder with children; its `accept` does the walking |
| `EntryVisitor` | Visitor: `visitFile`, `enterFolder`, `leaveFolder` |
| `ListingVisitor` | Lists the tree with indentation |
| `SizeVisitor` | Adds up the file sizes: 15 for the sample tree |
| `Main` | Runs both visitors over one tree |

## Diagram

![Visitor - Folder Tree (Homework) - Class Diagram](uml/Visitor%20-%20Folder%20Tree%20(Homework)%20-%20Class%20Diagram.png)

## Tests

`FileTreeTest` checks the behavior above and the exact output of `Main`. It is a unit test.

## Run it with

```bash
cd ~/"Development/Java/Idea/Design Patterns/Design Patterns with Java"
mvn -o -q test -Dtest=FileTreeTest
mvn -o -q compile
java -cp target/classes dev.kaldiroglu.dp.behavioral.visitor.hw.filetree.Main
```
