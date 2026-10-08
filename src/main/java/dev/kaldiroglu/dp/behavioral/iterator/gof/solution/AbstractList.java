package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

/**
 * The <b>Aggregate</b>: GoF's {@code AbstractList}.
 * <p>
 * {@link #createIterator()} is a factory method. Each kind of list creates the iterator that
 * knows its own structure, so a client that holds an {@code AbstractList} can walk an array
 * or a chain of nodes with the same code. GoF call this polymorphic iteration.
 */
public abstract class AbstractList<T> {

    public abstract Iterator<T> createIterator();

    public abstract int count();

    public abstract void append(T item);
}
