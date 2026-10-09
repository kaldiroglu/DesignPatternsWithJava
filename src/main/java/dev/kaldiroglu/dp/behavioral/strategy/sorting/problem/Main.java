package dev.kaldiroglu.dp.behavioral.strategy.sorting.problem;

import java.util.Arrays;
import java.util.Random;

/**
 * Sorts arrays of three sizes with one class that both chooses the algorithm and contains all
 * three.
 */
public class Main {

    public static void main(String[] args) {
        Sorter sorter = new Sorter();

        double[] small = {5, 3, 9, 1, 7};
        sorter.sort(small);
        System.out.println("Sorted " + Arrays.toString(small) + " with " + sorter.lastUsed());

        for (int size : new int[] {5_000, 1_000_000}) {
            double[] list = new Random(42).doubles(size, 0, 1000).toArray();
            sorter.sort(list);
            System.out.println("Sorted " + size + " numbers with " + sorter.lastUsed());
        }
        System.out.println("One class holds the choice and all three algorithms.");
    }
}
