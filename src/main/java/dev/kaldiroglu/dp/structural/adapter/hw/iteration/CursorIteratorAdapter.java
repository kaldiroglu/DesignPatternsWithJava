package dev.kaldiroglu.dp.structural.adapter.hw.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The Adapter — and the exercise is the method the adaptee <strong>cannot</strong> support.
 * <p>
 * {@code hasNext} and {@code next} map cleanly onto {@code hasMoreElements} and
 * {@code nextElement}. {@code remove} does not map onto anything: a {@link Cursor} has no
 * notion of removal, and no amount of translation invents one.
 * <p>
 * GoF are direct about this (p. 141): an adapter's usefulness is limited by how much of the
 * target interface the adaptee can actually support. Java's own answer is the same as the one
 * taken here — {@link Iterator#remove()} has a default that throws, and
 * {@code UnsupportedOperationException} exists precisely for the case where a type must
 * declare a method it cannot honor.
 * <p>
 * The honest thing is to throw, loudly, with a message that says why. The dishonest thing —
 * and the tempting one — is to make {@code remove} do nothing, so that callers silently get
 * a collection they believe they have modified.
 */
public class CursorIteratorAdapter<T> implements Iterator<T> {

    private final Cursor<T> cursor;

    public CursorIteratorAdapter(Cursor<T> cursor) {
        this.cursor = Objects.requireNonNull(cursor);
    }

    @Override
    public boolean hasNext() {
        return cursor.hasMoreElements();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("the cursor is exhausted");
        }
        return cursor.nextElement();
    }

    /** The one operation that cannot be adapted, and says so rather than pretending. */
    @Override
    public void remove() {
        throw new UnsupportedOperationException(
                "a Cursor cannot remove: the adaptee has no such operation");
    }
}
