package dev.kaldiroglu.dp.structural.composite.fileSystem;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * The <b>Component</b>: everything a file system holds can do these, file or directory.
 *
 * <p>Read the operations in two groups, because they are not the same kind of thing.</p>
 *
 * <p><b>The roll-ups</b> — {@link #size()}, {@link #count()}, {@link #lastModified()},
 * {@link #largest()}, {@link #find(String)}, {@link #findAll(Predicate)} and
 * {@link #render(String)}. Each asks one element a question and gets an answer for the whole
 * subtree beneath it. These are why the pattern is here: a caller writes {@code root.size()}
 * and no loop, at any depth, without knowing what it is holding. They are also, not by
 * accident, exactly the operations a real file system does <em>not</em> branch on —
 * {@code du}, {@code find} and {@code ls -lt} treat a file and a directory alike.</p>
 *
 * <p><b>The element operations</b> — {@link #rename}, {@link #save()}, {@link #delete()},
 * {@link #copy()} and {@link #move}. These act on one element and are on the Component
 * because every element needs them, not because they aggregate anything. Worth naming: a
 * Component interface grows, and GoF list "can make your design overly general" among this
 * pattern's liabilities.</p>
 *
 * <p>Child management lives on {@link StorageContainer}, not here — the <strong>safe</strong>
 * side of GoF's implementation issue 4 (Declaring the child management operations,
 * p. 168), the same choice {@code composite.graphic} makes.</p>
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

    // ------------------------------------------------------------------ the roll-ups

    /** Bytes, counting the whole subtree. A <b>sum</b>. */
    long size();

    /** Elements in the whole subtree, this one included. A file is one. A <b>sum</b>. */
    int count();

    /**
     * The newest modification anywhere beneath this element, itself included. A <b>maximum</b>,
     * which is worth noticing: aggregating is not only adding. Every file manager shows a
     * folder's date this way.
     */
    Instant lastModified();

    /**
     * The biggest leaf in the subtree — a reduction that returns an <b>element</b> rather
     * than a number. Empty only for an empty directory.
     */
    Optional<Storage> largest();

    /** The first element anywhere beneath this one with that name, this one included. */
    Optional<Storage> find(String name);

    /** Everything in the subtree matching {@code test}, in depth-first order. */
    List<Storage> findAll(Predicate<Storage> test);

    /** This element and everything under it, as text. */
    String render(String indent);
}
