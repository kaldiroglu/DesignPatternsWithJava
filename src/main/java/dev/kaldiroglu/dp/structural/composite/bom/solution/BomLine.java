package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

/**
 * One line of a bill of materials: a component and how many of it the parent
 * assembly requires.
 *
 * <p>Putting the quantity on the <em>edge</em> rather than the node is what lets
 * a single {@link Assembly} instance be shared by several parents — a bicycle
 * needs two identical wheels, not two wheel objects. GoF discusses this under
 * "Sharing components" (p. 167), and points at Flyweight (p. 195) as the solution
 * for pushing it further.</p>
 *
 * @param component the child component
 * @param quantity  how many of it are required, always at least one
 */
public record BomLine(BomComponent component, int quantity) {

    public BomLine {
        if (component == null) {
            throw new IllegalArgumentException("component must not be null");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be at least 1, was " + quantity);
        }
    }

    /** The cost contributed by this line: the child's total cost, times the quantity. */
    public Money extendedCost() {
        return component.totalCost().times(quantity);
    }

    /** The mass contributed by this line, in grams. */
    public int extendedWeightGrams() {
        return component.totalWeightGrams() * quantity;
    }

    /** The number of purchasable parts contributed by this line. */
    public int extendedPartCount() {
        return component.partCount() * quantity;
    }
}
