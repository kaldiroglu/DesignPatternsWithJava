package dev.kaldiroglu.dp.behavioral.strategy.sorting.pattern;

/**
 * The <b>Context</b>, and the slide this example exists for.
 * <p>
 * Read the branch below, then read the one in {@code problem.Sorter}. They test the same two
 * thresholds and they are not the same thing at all. That one <b>implemented</b> three
 * algorithms; this one <b>selects</b> between three objects and implements none.
 * <p>
 * That is the honest version of "Strategy removes your if statements". It does not. It
 * separates deciding from doing, and leaves the deciding somewhere small — which is worth
 * saying out loud, because a deck that promises the branch disappears is teaching something
 * the code does not do.
 * <p>
 * The cost is stated in GoF's consequences and is real: as algorithms are added, this method
 * grows. A registry keyed on the input, as {@code pricing.CampaignBook} uses, is the usual
 * next step.
 */
public final class SortingContext {

    private final Sorter bubbleSorter = new BubbleSorter();
    private final Sorter quickSorter = new QuickSorter();
    private final Sorter javaSorter = new JavaSorter();

    /** Below this many elements, bubbling beats setting anything else up. */
    public static final int BUBBLE_LIMIT = 100;

    /** Above this many, hand it to the library. */
    public static final int QUICK_LIMIT = 1_000_000;

    private String lastUsed = "none";

    public String lastUsed() {
        return lastUsed;
    }

    /** Choose an algorithm for this array, then let it do the work. */
    public void sort(double[] list) {
        Sorter sorter = sorterFor(list.length);
        lastUsed = sorter.name();
        sorter.sort(list);
    }

    /** The decision, on its own and testable without sorting anything. */
    public Sorter sorterFor(int size) {
        if (size < BUBBLE_LIMIT) {
            return bubbleSorter;
        }
        if (size < QUICK_LIMIT) {
            return quickSorter;
        }
        return javaSorter;
    }
}
