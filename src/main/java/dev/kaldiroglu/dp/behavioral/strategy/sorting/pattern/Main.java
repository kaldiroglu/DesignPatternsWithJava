package dev.kaldiroglu.dp.behavioral.strategy.sorting.pattern;

import java.util.Arrays;
import java.util.Random;

/** Sorts arrays of three sizes through the context, which selects a sorter and implements none. */
public class Main {

    public static void main(String[] args) {
        SortingContext context = new SortingContext();

        double[] small = {5, 3, 9, 1, 7};
        context.sort(small);
        System.out.println("Sorted " + Arrays.toString(small) + " with " + context.lastUsed());

        for (int size : new int[] {5_000, 1_000_000}) {
            double[] list = new Random(42).doubles(size, 0, 1000).toArray();
            context.sort(list);
            System.out.println("Sorted " + size + " numbers with " + context.lastUsed());
        }
        System.out.println("For a billion numbers the context would choose "
                + context.sorterFor(1_000_000_000).name() + ", and nothing was sorted to find out.");
    }
}
