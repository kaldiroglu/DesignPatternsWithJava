package dev.kaldiroglu.dp.behavioral.iterator.gof.problem;

import java.util.Arrays;

/**
 * A list that walks itself: the position of the walk is stored in the list.
 * <p>
 * This is the design GoF's motivation argues against (p. 257). The four traversal
 * operations — {@code first}, {@code next}, {@code isDone}, {@code currentItem} — are on the
 * list, and so is the cursor they move. It works for one loop. What it costs:
 * <ul>
 *   <li>Only one walk at a time. A loop inside a loop moves the same cursor, so the outer
 *       loop ends after its first item.</li>
 *   <li>A second order — backwards, or every second item — means more operations and a
 *       second cursor on the list itself.</li>
 * </ul>
 */
public final class CursorList<T> {

    private Object[] items = new Object[4];
    private int count;
    private int cursor;                       // the walk's position, stored in the list

    public void append(T item) {
        if (count == items.length) {
            items = Arrays.copyOf(items, count * 2);
        }
        items[count++] = item;
    }

    public int count() {
        return count;
    }

    public void first() {
        cursor = 0;
    }

    public void next() {
        cursor++;
    }

    public boolean isDone() {
        return cursor >= count;
    }

    @SuppressWarnings("unchecked")
    public T currentItem() {
        return (T) items[cursor];
    }
}
