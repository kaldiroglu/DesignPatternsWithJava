package dev.kaldiroglu.dp.behavioral.strategy.sorting.pattern;

/**
 * The <b>Strategy</b>: one way of sorting an array.
 * <p>
 * The same three algorithms as the two stages before it, and the same code inside them. What
 * changed is who holds them: nothing extends this, and nothing that holds one had to choose
 * it.
 */
public interface Sorter {

    String name();

    void sort(double[] list);
}
