package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

/**
 * The <b>Iterator</b>, with GoF's four operations.
 * <p>
 * The position of the walk lives in the iterator, not in the list. So any number of
 * iterators can walk the same list at the same time.
 * <p>
 * {@code java.util.Iterator} has the same job with two operations: {@code hasNext()} is
 * {@code !isDone()}, and {@code next()} is {@code currentItem()} followed by {@code next()}.
 * Starting again ({@code first()}) means asking the list for a new iterator.
 */
public interface Iterator<T> {

    void first();

    void next();

    boolean isDone();

    T currentItem();
}
