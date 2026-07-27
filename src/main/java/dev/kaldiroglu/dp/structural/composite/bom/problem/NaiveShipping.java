package dev.kaldiroglu.dp.structural.composite.bom.problem;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

/**
 * The second client of the naive design: the shipping calculator, written months
 * later by a different team.
 *
 * <p>They could not reuse anything from {@link NaiveCosting}, because there is
 * nothing to reuse — the recursion is welded to the thing being computed. So
 * they wrote the walk again. It is now in this codebase three times, and every
 * future client will make it four.</p>
 *
 * <p>The bug this invites is not hypothetical. If a fourth collection is ever
 * added to {@link Assembly}, or if someone forgets the
 * {@code subAssemblies} loop in one of the three copies, the answer is silently
 * wrong — no exception, no compiler error, just a quotation that is too low.</p>
 */
public final class NaiveShipping {

    private static final Money RATE_PER_KILO = Money.of(4.90);

    private NaiveShipping() {
    }

    /**
     * The mass of an item in grams — the same walk as
     * {@link NaiveCosting#totalCost(Object)}, for the third time.
     */
    public static int totalWeightGrams(Object node) {
        if (node instanceof Part part) {
            return part.weightGrams();
        }
        if (node instanceof Assembly assembly) {
            int total = assembly.assemblyWeightGrams();
            for (Part part : assembly.parts()) {
                total += part.weightGrams();
            }
            for (Assembly sub : assembly.subAssemblies()) {
                total += totalWeightGrams(sub);
            }
            return total;
        }
        throw new IllegalArgumentException(
                "NaiveShipping cannot weigh a " + node.getClass().getSimpleName());
    }

    /** What it costs to ship an item, rounded up to the next whole kilo. */
    public static Money estimate(Object node) {
        int kilos = Math.max(1, (totalWeightGrams(node) + 999) / 1000);
        return RATE_PER_KILO.times(kilos);
    }
}
