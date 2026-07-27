package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

/**
 * Leaf role of the Composite pattern — a purchased part that is not broken down
 * any further.
 *
 * <p>A rim, a spoke, a bearing. It has a price from a supplier and a mass, and
 * it answers every {@link BomComponent} query from its own two fields. There is
 * no recursion here: this is where the recursion stops.</p>
 */
public final class Part extends BomComponent {

    private final Money unitCost;
    private final int weightGrams;

    public Part(String partNumber, String name, Money unitCost, int weightGrams) {
        super(partNumber, name);
        if (weightGrams < 0) {
            throw new IllegalArgumentException("weight must not be negative");
        }
        this.unitCost = unitCost;
        this.weightGrams = weightGrams;
    }

    /** Creates a part from the shared reference data. */
    public Part(Catalog.PartSpec spec) {
        this(spec.partNumber(), spec.name(), spec.unitCost(), spec.weightGrams());
    }

    /** The supplier's price for one of these. */
    public Money unitCost() {
        return unitCost;
    }

    @Override
    public Money totalCost() {
        return unitCost;
    }

    @Override
    public int totalWeightGrams() {
        return weightGrams;
    }

    @Override
    public int partCount() {
        return 1;
    }
}
