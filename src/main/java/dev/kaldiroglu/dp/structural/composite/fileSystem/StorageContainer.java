package dev.kaldiroglu.dp.structural.composite.fileSystem;

import dev.kaldiroglu.dp.structural.composite.fileSystem.iterator.StorageIterator;

import java.util.List;

/**
 * Child management, kept off {@link Storage} on purpose — GoF implementation issue 4
 * (Declaring the child management operations, p. 168).
 * <p>
 * Declaring {@code add} here rather than on the Component means a {@link File} cannot be
 * given children: the method does not exist on it, so the mistake is a compile error rather
 * than a run-time one. The price is that code building a tree has to know it is holding a
 * {@link Directory}.
 */
public interface StorageContainer {

    void add(Storage element);

    void remove(Storage element);

    List<Storage> elements();

    /** Depth-first over the whole subtree, not just the immediate children. */
    StorageIterator iterator();
}
