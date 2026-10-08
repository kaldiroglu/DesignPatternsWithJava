package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

/** A <b>ConcreteIterator</b> for {@link List}: front to back, by index. */
public final class ListIterator<T> implements Iterator<T> {

    private final List<T> list;
    private int current;

    public ListIterator(List<T> list) {
        this.list = list;
    }

    @Override
    public void first() {
        current = 0;
    }

    @Override
    public void next() {
        current++;
    }

    @Override
    public boolean isDone() {
        return current >= list.count();
    }

    @Override
    public T currentItem() {
        if (isDone()) {
            throw new IllegalStateException("the walk is over");
        }
        return list.get(current);
    }
}
