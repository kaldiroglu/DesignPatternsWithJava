package dev.kaldiroglu.dp.behavioral.strategy.sorting.pattern;

import java.util.Arrays;

/** Hand it to the library, which is the right answer once the array is big enough. */
public final class JavaSorter implements Sorter {

    @Override
    public String name() {
        return "JavaSort";
    }

    @Override
    public void sort(double[] list) {
        Arrays.sort(list);
    }
}
