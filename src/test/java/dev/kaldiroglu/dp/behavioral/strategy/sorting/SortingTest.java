package dev.kaldiroglu.dp.behavioral.strategy.sorting;

import dev.kaldiroglu.dp.behavioral.strategy.sorting.pattern.SortingContext;
import dev.kaldiroglu.dp.behavioral.strategy.sorting.pattern.Sorter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * The author's own Strategy example: three sorting algorithms, and which one suits the array
 * decided by its size. It makes a point the pricing example does not — the branch does not
 * disappear when the pattern is applied. It stops implementing and starts selecting.
 */
class SortingTest {

    private static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/behavioral/strategy/sorting/";

    private static double[] shuffled(int size) {
        Random random = new Random(42);          // fixed seed: the same array every run
        double[] list = new double[size];
        for (int i = 0; i < size; i++) {
            list[i] = random.nextDouble() * 1000;
        }
        return list;
    }

    private static double[] sortedCopy(double[] list) {
        double[] copy = list.clone();
        Arrays.sort(copy);
        return copy;
    }

    @Test
    @DisplayName("all three designs sort the same array into the same order")
    void theDesignsAgree() {
        double[] expected = sortedCopy(shuffled(50));

        double[] byBranch = shuffled(50);
        new dev.kaldiroglu.dp.behavioral.strategy.sorting.problem.Sorter().sort(byBranch);

        double[] bySubclass = shuffled(50);
        new dev.kaldiroglu.dp.behavioral.strategy.sorting.subclassing.BubbleSorter()
                .sort(bySubclass);

        double[] byStrategy = shuffled(50);
        new SortingContext().sort(byStrategy);

        assertArrayEquals(expected, byBranch);
        assertArrayEquals(expected, bySubclass);
        assertArrayEquals(expected, byStrategy);
    }

    @Test
    @DisplayName("the size decides, and both designs decide the same way")
    void theSameThresholds() {
        var naive = new dev.kaldiroglu.dp.behavioral.strategy.sorting.problem.Sorter();
        var context = new SortingContext();

        for (int size : new int[]{10, 99, 100, 5_000}) {
            double[] a = shuffled(size);
            double[] b = a.clone();
            naive.sort(a);
            context.sort(b);
            assertEquals(naive.lastUsed(), context.lastUsed(), "size " + size);
        }

        assertEquals("BubbleSort", context.sorterFor(99).name());
        assertEquals("QuickSort", context.sorterFor(100).name());
        assertEquals("JavaSort", context.sorterFor(1_000_000).name());
    }

    @Test
    @DisplayName("the decision can be tested without sorting anything")
    void theDecisionIsSeparable() {
        SortingContext context = new SortingContext();

        // The whole point of separating deciding from doing: this asks which algorithm the
        // context would choose for a billion elements without allocating a billion doubles.
        assertEquals("JavaSort", context.sorterFor(1_000_000_000).name());
        assertSame(context.sorterFor(50), context.sorterFor(60));
        assertNotSame(context.sorterFor(50), context.sorterFor(5_000));
    }

    @Test
    @DisplayName("the branch survives the pattern — but it selects instead of implementing")
    void theBranchMovedRatherThanVanished() throws Exception {
        String naive = Files.readString(Path.of(SOURCE + "problem/Sorter.java"));
        String context = Files.readString(Path.of(SOURCE + "pattern/SortingContext.java"));
        String code = context.replaceAll("(?s)/\\*.*?\\*/", " ").replaceAll("//[^\n]*", " ");

        // Both test the same two thresholds.
        assertTrue(naive.contains("100") && naive.contains("1_000_000"));
        assertTrue(code.contains("BUBBLE_LIMIT") && code.contains("QUICK_LIMIT"));

        // The naive class carries the algorithms too: a bubble pass, a quicksort and a
        // partition. The context carries none of them.
        assertTrue(naive.contains("quicksort(") && naive.contains("partition("));
        assertTrue(code.contains("return bubbleSorter") && !code.contains("partition("),
                "the context selects; it must not implement");

        // Measured rather than described: the naive class is several times the size of the
        // one that only decides.
        int naiveLines = naive.split("\n").length;
        int contextLines = context.split("\n").length;
        assertTrue(naiveLines > contextLines,
                naiveLines + " lines against " + contextLines);
    }

    @Test
    @DisplayName("a fourth algorithm is one class, and the context is the only edit")
    void addingAnAlgorithm() {
        Sorter insertion = new Sorter() {
            @Override
            public String name() {
                return "InsertionSort";
            }

            @Override
            public void sort(double[] list) {
                for (int i = 1; i < list.length; i++) {
                    double key = list[i];
                    int j = i - 1;
                    while (j >= 0 && list[j] > key) {
                        list[j + 1] = list[j];
                        j--;
                    }
                    list[j + 1] = key;
                }
            }
        };

        double[] list = shuffled(30);
        double[] expected = sortedCopy(list);
        insertion.sort(list);

        assertArrayEquals(expected, list);
        assertEquals("InsertionSort", insertion.name());
        // Nothing in pattern/ was edited to write this, which is the half the naive designs
        // could not manage. Choosing it, though, still means touching SortingContext — the
        // cost GoF list and the reason a registry usually follows.
    }
}
