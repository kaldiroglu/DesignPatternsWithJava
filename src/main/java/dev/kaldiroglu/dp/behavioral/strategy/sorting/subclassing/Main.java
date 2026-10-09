package dev.kaldiroglu.dp.behavioral.strategy.sorting.subclassing;

import java.util.Arrays;

/** Sorts one array with each subclass, and shows that the caller now has to choose which one. */
public class Main {

    public static void main(String[] args) {
        Sorter[] sorters = {new BubbleSorter(), new QuickSorter(), new JavaSorter()};
        for (Sorter sorter : sorters) {
            double[] list = {5, 3, 9, 1, 7};
            sorter.sort(list);
            System.out.println(sorter.name() + ": " + Arrays.toString(list));
        }
        System.out.println("Each algorithm is its own class, and nothing here chooses by size.");
        System.out.println("Every caller has to know which class suits which array.");
    }
}
