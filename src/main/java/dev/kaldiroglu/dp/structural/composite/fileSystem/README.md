<!--
Model: Claude Opus 5 (claude-opus-5)
Created: 2026-07-28
-->

# `composite.fileSystem` — a file system, with a recursive iterator

*For further enquiry please contact Akin Kaldiroglu at akin@kaldiroglu.dev*

The example everybody already has an intuition for. A directory holds files; it also holds
other directories, which hold files of their own. Ask any element how big it is and you get
an answer for the whole subtree beneath it — one call, at any depth, with no loop at the
call site.

## The participants

| Class | Role | What it does |
|---|---|---|
| `Storage` | **Component** | The interface every element answers: `size()`, `render()`, `copy()`, `move()`, `delete()`, `rename()`, `save()` |
| `File` | **Leaf** | Has a size of its own and no children |
| `Link` · `Alias` · `ShortCut` | **Leaf** | Point at another element and name it |
| `Directory` | **Composite** | Holds `Storage` — files, links or more directories — and answers by asking them |
| `StorageElement` | — | Shared state: the name, and the parent it hangs from |
| `StorageContainer` | — | Child management, deliberately kept off the Component |

## The one idea

Every operation on `Directory` is the same two lines: do this directory's part, then ask the
children to do theirs.

```java
public long size() {
    return DIRECTORY_BYTES + elements.stream().mapToLong(Storage::size).sum();
}
```

That is the whole pattern. `render()`, `copy()` and `save()` have the same shape. The
recursion lives here, once — a client that wants the size of a tree writes `root.size()` and
nothing else, and it neither knows nor cares how deep the tree goes.

Note what is **not** in `Directory.render()`: it never asks what kind of element it is
holding. Each element renders itself. Taking that question away from callers is the reason to
reach for this pattern at all.

## Two decisions worth arguing about

**Child management is on `StorageContainer`, not on `Storage`.** This is GoF's implementation
issue 1, and this example takes the **safe** side: `add` does not exist on `File`, so giving
a file children is a compile error rather than a run-time one. The price is that code
building a tree has to know it is holding a `Directory`. `composite.graphic` makes the same
choice and `composite.bom` makes the transparent one, so the two can be read side by side.

**A link costs its own 64 bytes, not its target's.** Any other answer makes `size()` on a
root double-count, because the target is already counted where it actually lives. A tree that
lies about its own size is worse than one that cannot be asked.

## The iterator

`DirectoryIterator` walks the whole subtree depth-first rather than returning the immediate
children, which is the only version worth having over a Composite: a nested directory is not
one item, it is everything underneath it.

## What the tests measure

`FileSystemCompositeTest` states the claims above as numbers rather than descriptions — that
`size()` on a root crosses three levels and totals every leaf under it, that `copy()` is a
genuine deep copy detached from any parent, that `move()` leaves the old parent and joins the
new one, and that the iterator reaches elements at every depth.

## Run it with

```bash
mvn -o test -Dtest=FileSystemCompositeTest
java -cp target/classes dev.kaldiroglu.dp.structural.composite.fileSystem.Main
```
