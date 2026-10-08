package dev.kaldiroglu.dp.behavioral.iterator.hw.paging;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Homework 3: walk every item of a paged result without loading every page.
 * <p>
 * The caller sees one long sequence and writes a simple loop. Behind it, the iterator asks
 * the source for the next page only when the current one is used up. A caller that stops
 * after the first match never causes the later pages to be fetched — {@link #pagesFetched()}
 * shows how many were.
 */
public final class PagedIterator<T> implements Iterator<T> {

    private final PageSource<T> source;
    private int pageNumber;
    private int pagesFetched;
    private Iterator<T> current = List.<T>of().iterator();
    private boolean finished;

    public PagedIterator(PageSource<T> source) {
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        while (!current.hasNext() && !finished) {
            List<T> page = source.page(pageNumber++);
            pagesFetched++;
            if (page.isEmpty()) {
                finished = true;
            }
            current = page.iterator();
        }
        return current.hasNext();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("no more items");
        }
        return current.next();
    }

    public int pagesFetched() {
        return pagesFetched;
    }
}
