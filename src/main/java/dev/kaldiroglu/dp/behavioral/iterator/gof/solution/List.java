package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

import java.util.Arrays;

/**
 * A <b>ConcreteAggregate</b> stored in an array: GoF's {@code List}.
 * <p>
 * It has no traversal operations and no cursor. Compare {@code problem.CursorList}.
 */
public final class List<T> extends AbstractList<T> {

    private Object[] items = new Object[4];
    private int count;

    @Override
    public void append(T item) {
        if (count == items.length) {
            items = Arrays.copyOf(items, count * 2);
        }
        items[count++] = item;
    }

    @Override
    public int count() {
        return count;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException(index);
        }
        return (T) items[index];
    }

    @Override
    public Iterator<T> createIterator() {
        return new ListIterator<>(this);
    }
}
