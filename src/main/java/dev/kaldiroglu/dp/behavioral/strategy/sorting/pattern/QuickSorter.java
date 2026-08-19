package dev.kaldiroglu.dp.behavioral.strategy.sorting.pattern;

/** The middle case: too big to bubble, small enough that the library's setup is not free. */
public final class QuickSorter implements Sorter {

    @Override
    public String name() {
        return "QuickSort";
    }

    @Override
    public void sort(double[] list) {
        quicksort(list, 0, list.length - 1);
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
