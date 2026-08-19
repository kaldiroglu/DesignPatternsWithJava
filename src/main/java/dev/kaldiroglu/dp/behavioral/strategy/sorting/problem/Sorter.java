package dev.kaldiroglu.dp.behavioral.strategy.sorting.problem;

import java.util.Arrays;

/**
 * Stage one: three sorting algorithms in one class, chosen by a branch on the array's size.
 * <p>
 * A small array is quickest to bubble; a large one is best left to the library. So the
 * choice is genuine engineering rather than indecision — which is what makes this design
 * worth taking seriously before it is taken apart.
 * <p>
 * What it costs is that the class does two different jobs at once: it <b>decides</b> which
 * algorithm suits the input, and it <b>implements</b> all three. Neither can be changed,
 * read or tested without the other.
 */
public final class Sorter {

    /** How the last call was sorted, so a test can see the decision rather than infer it. */
    private String lastUsed = "none";

    public String lastUsed() {
        return lastUsed;
    }

    public void sort(double[] list) {
        int size = list.length;

        if (size < 100) {
            lastUsed = "BubbleSort";
            for (int counter = 0; counter < size - 1; counter++) {
                for (int index = 0; index < size - 1 - counter; index++) {
                    if (list[index] > list[index + 1]) {
                        double temp = list[index];
                        list[index] = list[index + 1];
                        list[index + 1] = temp;
                    }
                }
            }
        } else if (size < 1_000_000) {
            lastUsed = "QuickSort";
            quicksort(list, 0, size - 1);
        } else {
            lastUsed = "JavaSort";
            Arrays.sort(list);
        }
    }

    private void quicksort(double[] a, int left, int right) {
        if (right <= left) {
            return;
        }
        int i = partition(a, left, right);
        quicksort(a, left, i - 1);
        quicksort(a, i + 1, right);
    }

    private int partition(double[] a, int left, int right) {
        int i = left;
        int j = right;
        while (true) {
            while (a[i] < a[right]) {
                i++;
            }
            while (a[right] < a[--j]) {
                if (j == left) {
                    break;
                }
            }
            if (i >= j) {
                break;
            }
            exchange(a, i, j);
        }
        exchange(a, i, right);
        return i;
    }

    private void exchange(double[] a, int i, int j) {
        double swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }
}
