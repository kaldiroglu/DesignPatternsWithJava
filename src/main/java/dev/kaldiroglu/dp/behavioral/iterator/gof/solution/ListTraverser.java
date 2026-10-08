package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

/**
 * An <b>internal iterator</b>: GoF's {@code ListTraverser}.
 * <p>
 * The traverser runs the loop, and a subclass says what to do with each item in
 * {@link #processItem}. Returning {@code false} stops the walk early. The client writes no
 * loop at all — but it also cannot walk two lists side by side, because each traverser runs
 * its own loop to the end. That is GoF implementation issue 1 (who controls the iteration?).
 */
public abstract class ListTraverser<T> {

    private final Iterator<T> iterator;

    protected ListTraverser(AbstractList<T> list) {
        this.iterator = list.createIterator();
    }

    /** Answers true if every item was processed, false if the walk stopped early. */
    public boolean traverse() {
        for (iterator.first(); !iterator.isDone(); iterator.next()) {
            if (!processItem(iterator.currentItem())) {
                return false;
            }
        }
        return true;
    }

    protected abstract boolean processItem(T item);
}
