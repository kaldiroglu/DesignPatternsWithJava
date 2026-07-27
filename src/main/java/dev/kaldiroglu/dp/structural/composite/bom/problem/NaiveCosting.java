package dev.kaldiroglu.dp.structural.composite.bom.problem;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

/**
 * The first client of the naive design: the quotation screen's costing routine.
 *
 * <p>Because {@link Part} and {@link Assembly} share no supertype, the operation
 * cannot be a method on the thing being costed. It becomes a static function
 * that takes {@link Object} — and the moment a parameter is {@code Object}, the
 * type system has stopped helping.</p>
 *
 * <p>Read the two methods below and notice that they are the <em>same walk,
 * written twice</em>. Then read {@link NaiveShipping} and notice it is the same
 * walk a third time. That is the duplication Composite removes: in
 * {@code ..bom.solution} the walk appears once, inside the composite itself, and
 * every client is a single method call.</p>
 */
public final class NaiveCosting {

    private NaiveCosting() {
    }

    /**
     * The total cost of an item, whatever kind of item it is.
     *
     * @param node a {@link Part} or an {@link Assembly}
     * @throws IllegalArgumentException for anything else — including
     *         {@link Service}, which was added to the domain later
     */
    public static Money totalCost(Object node) {
        if (node instanceof Part part) {
            return part.unitCost();
        }
        if (node instanceof Assembly assembly) {
            Money total = assembly.assemblyCost();
            for (Part part : assembly.parts()) {          // first collection
                total = total.plus(part.unitCost());
            }
            for (Assembly sub : assembly.subAssemblies()) { // second collection
                total = total.plus(totalCost(sub));
            }
            return total;
        }
        // Every new kind of item lands here until somebody edits this method.
        throw new IllegalArgumentException(
                "NaiveCosting cannot cost a " + node.getClass().getSimpleName());
    }

    /**
     * The number of purchasable parts in an item.
     *
     * <p>The same recursion again, with two words changed. A reader has to
     * compare it line by line with {@link #totalCost(Object)} to be sure it visits
     * the same nodes — and it is exactly this kind of near-duplicate that quietly
     * drifts out of step when the structure changes.</p>
     */
    public static int partCount(Object node) {
        if (node instanceof Part) {
            return 1;
        }
        if (node instanceof Assembly assembly) {
            int total = 0;
            for (Part ignored : assembly.parts()) {
                total++;
            }
            for (Assembly sub : assembly.subAssemblies()) {
                total += partCount(sub);
            }
            return total;
        }
        throw new IllegalArgumentException(
                "NaiveCosting cannot count a " + node.getClass().getSimpleName());
    }
}
