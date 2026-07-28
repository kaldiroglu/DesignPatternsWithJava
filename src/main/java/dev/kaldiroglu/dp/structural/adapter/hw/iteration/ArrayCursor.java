package dev.kaldiroglu.dp.structural.adapter.hw.iteration;

import java.util.List;

/** A concrete adaptee over a fixed list. */
public class ArrayCursor<T> implements Cursor<T> {

    private final List<T> items;
    private int position;

    public ArrayCursor(List<T> items) {
        this.items = List.copyOf(items);
    }

    @Override
    public boolean hasMoreElements() {
        return position < items.size();
    }

    @Override
    public T nextElement() {
        if (!hasMoreElements()) {
            throw new IllegalStateException("the cursor is exhausted");
        }
        return items.get(position++);
    }
}
