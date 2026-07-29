package dev.kaldiroglu.dp.structural.composite.fileSystem.iterator;

import dev.kaldiroglu.dp.structural.composite.fileSystem.Directory;
import dev.kaldiroglu.dp.structural.composite.fileSystem.Storage;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Walks a directory tree depth-first.
 * <p>
 * Depth-first over the whole subtree, not just the immediate children. That is the only
 * version worth having over a Composite: a nested directory is not one item, it is
 * everything underneath it, and the reason to have an iterator at all is to reach the whole
 * tree without the caller writing the recursion.
 * <p>
 * GoF mention exactly this under Composite's implementation notes: enumerating children is a
 * job for an Iterator, and traversal is where the two patterns meet.
 */
public class DirectoryIterator implements StorageIterator {

    private final Deque<Storage> pending = new ArrayDeque<>();

    public DirectoryIterator(Directory root) {
        pushAll(root.elements());
    }

    @Override
    public boolean hasNext() {
        return !pending.isEmpty();
    }

    @Override
    public Storage next() {
        if (pending.isEmpty()) {
            throw new NoSuchElementException("the tree has been walked to the end");
        }
        Storage next = pending.pop();
        if (next instanceof Directory directory) {
            pushAll(directory.elements());
        }
        return next;
    }

    /** Pushed in reverse so siblings come back in the order they were added. */
    private void pushAll(List<Storage> elements) {
        for (int i = elements.size() - 1; i >= 0; i--) {
            pending.push(elements.get(i));
        }
    }
}
