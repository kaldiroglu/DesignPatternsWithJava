package dev.kaldiroglu.dp.structural.composite.fileSystem.iterator;

import dev.kaldiroglu.dp.structural.composite.fileSystem.Storage;

import java.util.Iterator;

/**
 * An iterator over storage elements.
 * <p>
 * The interface adds nothing to {@link Iterator} and is here for the name: code that takes a
 * {@code StorageIterator} says what it walks, and the file system can change how it walks
 * without touching that code.
 */
public interface StorageIterator extends Iterator<Storage> {
}
