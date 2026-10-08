package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

/**
 * A <b>ConcreteIterator</b> for {@link ChainList}: it follows the links.
 * <p>
 * It reads the list's nodes, which no client can see. GoF implementation issue 6
 * (iterators may have privileged access): an iterator is part of its list's design, so it
 * may know things the public interface does not show. Here that is package access.
 */
public final class ChainListIterator<T> implements Iterator<T> {

    private final ChainList<T> list;
    private ChainList.Node<T> current;

    public ChainListIterator(ChainList<T> list) {
        this.list = list;
    }

    @Override
    public void first() {
        current = list.head();
    }

    @Override
    public void next() {
        current = current.next;
    }

    @Override
    public boolean isDone() {
        return current == null;
    }

    @Override
    public T currentItem() {
        if (isDone()) {
            throw new IllegalStateException("the walk is over");
        }
        return current.item;
    }
}
