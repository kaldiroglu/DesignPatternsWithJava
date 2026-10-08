package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

/**
 * A second <b>ConcreteAggregate</b>, stored as a chain of nodes.
 * <p>
 * GoF use a skip list here. Any structure that is not an array makes the same point: it
 * cannot be walked by index cheaply, so it creates its own kind of iterator, one that
 * follows the links. A client that walks an {@link AbstractList} does not see the difference.
 */
public final class ChainList<T> extends AbstractList<T> {

    static final class Node<T> {
        final T item;
        Node<T> next;

        Node(T item) {
            this.item = item;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int count;

    @Override
    public void append(T item) {
        Node<T> node = new Node<>(item);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
        count++;
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public Iterator<T> createIterator() {
        return new ChainListIterator<>(this);
    }

    Node<T> head() {
        return head;
    }
}
