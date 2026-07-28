package dev.kaldiroglu.dp.structural.composite.fileSystem;

/**
 * The Component: everything a file system holds can do these, file or directory.
 * <p>
 * {@code size()} is the operation worth having. A client asks one element how big it is and
 * gets an answer for the whole subtree beneath it, without knowing or caring whether it is
 * holding one file or ten thousand.
 * <p>
 * Child management lives on {@link StorageContainer}, not here — the <strong>safe</strong>
 * side of GoF's implementation issue 1, the same choice {@code composite.graphic} makes.
 */
public interface Storage {

    String getName();

    void rename(String newName);

    void save();

    /** Detaches this element from its parent. Harmless on a root. */
    void delete();

    /** A deep copy, detached from any parent. */
    Storage copy();

    /** Moves this element into {@code target}, leaving its old parent behind. */
    void move(Directory target);

    /** Bytes, counting the whole subtree. */
    long size();

    /** Renders this element and anything below it, indented one level per depth. */
    String render(String indent);
}
