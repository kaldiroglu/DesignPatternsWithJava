package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

/**
 * A second kind of Leaf, added to the design after everything else was written:
 * a subcontracted operation such as powder coating.
 *
 * <p>It costs money, it adds no mass, and it is not a part anyone can put on a
 * shelf — so it answers the three roll-up questions differently from
 * {@link Part}: a fee, zero grams, and <b>zero</b> parts.</p>
 *
 * <p>This class is the whole change. Nothing else in the package was touched,
 * and no client anywhere — costing, shipping, the tree printer, the roll-ups in
 * {@link Assembly} — needed a single edit. They were all written against
 * {@link BomComponent}, so they handled this class correctly before it
 * existed.</p>
 *
 * <p>That is GoF's third consequence (p. 166): <em>"Newly defined Composite or
 * Leaf subclasses work automatically with existing structures and existing client
 * code."</em> Compare {@code problem.Service}, where the same requirement forces
 * a third collection on the assembly and a new branch in every client.</p>
 */
public final class Service extends BomComponent {

    private final Money fee;

    public Service(String partNumber, String name, Money fee) {
        super(partNumber, name);
        this.fee = fee;
    }

    /** Creates a service from the shared reference data. */
    public Service(Catalog.ServiceSpec spec) {
        this(spec.partNumber(), spec.name(), spec.fee());
    }

    /** What the subcontractor charges. */
    public Money fee() {
        return fee;
    }

    @Override
    public Money totalCost() {
        return fee;
    }

    /** An operation adds no mass. */
    @Override
    public int totalWeightGrams() {
        return 0;
    }

    /** An operation is not a purchasable part, so it counts as none. */
    @Override
    public int partCount() {
        return 0;
    }
}
