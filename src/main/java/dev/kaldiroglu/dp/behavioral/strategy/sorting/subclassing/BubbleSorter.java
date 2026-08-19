package dev.kaldiroglu.dp.behavioral.strategy.sorting.subclassing;

/** Quickest on a short array, and hopeless on a long one. */
public final class BubbleSorter extends Sorter {

    @Override
    public String name() {
        return "BubbleSort";
    }

    @Override
    public void sort(double[] list) {
        for (int counter = 0; counter < list.length - 1; counter++) {
            for (int index = 0; index < list.length - 1 - counter; index++) {
                if (list[index] > list[index + 1]) {
                    double temp = list[index];
                    list[index] = list[index + 1];
                    list[index + 1] = temp;
                }
            }
        }
    }
}
