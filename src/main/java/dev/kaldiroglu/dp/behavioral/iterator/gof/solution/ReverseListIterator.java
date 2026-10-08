package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

/**
 * A second <b>ConcreteIterator</b> for the same {@link List}: back to front.
 * <p>
 * GoF's {@code ReverseListIterator}. The list did not change to get a second order.
 */
public final class ReverseListIterator<T> implements Iterator<T> {

    private final List<T> list;
    private int current;

    public ReverseListIterator(List<T> list) {
        this.list = list;
    }

    @Override
    public void first() {
        current = list.count() - 1;
    }

    @Override
    public void next() {
        current--;
    }

    @Override
    public boolean isDone() {
        return current < 0;
    }

    @Override
    public T currentItem() {
        if (isDone()) {
            throw new IllegalStateException("the walk is over");
        }
        return list.get(current);
    }
}
