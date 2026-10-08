package dev.kaldiroglu.dp.behavioral.iterator.hw.paging;

import java.util.List;

/**
 * Where the pages come from: a web service, a database, a file. One call returns one page.
 * An empty page means there is nothing more.
 */
@FunctionalInterface
public interface PageSource<T> {

    List<T> page(int number);
}
