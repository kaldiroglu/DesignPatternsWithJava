package dev.kaldiroglu.dp.structural.composite.bom.problem;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

/**
 * The naive design's purchased part — a plain class with <b>no common supertype</b>
 * shared with {@link Assembly}.
 *
 * <p>Nothing here is wrong in itself. The class is small, clear and correct. The
 * damage is done by what is <em>missing</em>: because a {@code Part} and an
 * {@link Assembly} have no type in common, no client can be written against
 * "a thing in a bill of materials". Every client must ask which one it is
 * holding.</p>
 *
 * <p>Compare with {@code solution.Part}, which is the same three fields —
 * but extends {@code BomComponent}.</p>
 */
public class Part {

    private final String partNumber;
    private final String name;
    private final Money unitCost;
    private final int weightGrams;

    public Part(String partNumber, String name, Money unitCost, int weightGrams) {
        this.partNumber = partNumber;
        this.name = name;
        this.unitCost = unitCost;
        this.weightGrams = weightGrams;
    }

    /** Creates a part from the shared reference data. */
    public Part(Catalog.PartSpec spec) {
        this(spec.partNumber(), spec.name(), spec.unitCost(), spec.weightGrams());
    }

    public String partNumber() {
        return partNumber;
    }

    public String name() {
        return name;
    }

    public Money unitCost() {
        return unitCost;
    }

    public int weightGrams() {
        return weightGrams;
    }
}
