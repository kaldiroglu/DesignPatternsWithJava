package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

import java.util.List;

/**
 * Component role of the Composite solution — one entry in a bill of materials.
 *
 * <p>A bill of materials describes what a manufactured product is made of. A
 * bicycle contains wheels; a wheel contains a rim, spokes and a hub; a hub
 * contains an axle and bearings. The nesting has no fixed depth and differs from
 * product to product.</p>
 *
 * <p>The whole point of this abstraction is that the four questions the business
 * actually asks — <em>what does it cost, what does it weigh, how many parts is
 * it, and show me the structure</em> — are answered the same way for a single
 * bearing and for a finished bicycle. Client code such as a quotation screen or
 * a shipping calculator therefore never branches on "part or assembly?".</p>
 *
 * <p><b>On the safety/transparency trade-off (GoF, p. 168):</b> unlike the
 * book's own examples, this one declares {@link #add(BomComponent, int)} on
 * {@link Assembly} only, <em>not</em> here. A bill of materials is edited by a
 * small amount of engineering-change code that legitimately knows it is holding
 * an assembly, while the many read-only clients — costing, shipping, reporting —
 * only ever call the query operations below. Restricting the child operations to
 * the composite buys compile-time safety and costs those read-only clients
 * nothing. See {@code Composite - Problem and Solution.md} for the full argument.</p>
 */
public abstract class BomComponent {

    private final String partNumber;
    private final String name;

    protected BomComponent(String partNumber, String name) {
        this.partNumber = partNumber;
        this.name = name;
    }

    /** The catalog identifier, e.g. {@code "RIM-700C"}. */
    public String partNumber() {
        return partNumber;
    }

    /** The human-readable name, e.g. {@code "700c Rim"}. */
    public String name() {
        return name;
    }

    // --- The operations every client cares about, leaf and composite alike ---

    /** The total cost of this component including everything inside it. */
    public abstract Money totalCost();

    /** The total mass in grams of this component including everything inside it. */
    public abstract int totalWeightGrams();

    /**
     * The number of individual purchasable parts inside this component,
     * counting quantities. A part counts as one; an assembly counts the sum of
     * its lines.
     */
    public abstract int partCount();

    /**
     * The lines that make up this component — empty for a part.
     *
     * <p>A read-only view is safe to offer on a leaf, and it is what lets client
     * code walk any subtree recursively without a type test.</p>
     */
    public List<BomLine> lines() {
        return List.of();
    }

    /** Whether this component is an assembly that can contain other components. */
    public boolean isAssembly() {
        return false;
    }

    /**
     * Whether {@code target} appears anywhere strictly below this component.
     *
     * <p>Used by {@link Assembly#add(BomComponent, int)} to keep the product
     * structure acyclic. A leaf answers {@code false} without any special
     * casing, because it has no lines to search.</p>
     */
    boolean containsDeep(BomComponent target) {
        for (BomLine line : lines()) {
            if (line.component() == target || line.component().containsDeep(target)) {
                return true;
            }
        }
        return false;
    }

    /** Renders this component and everything below it as an indented tree. */
    public String toTree() {
        StringBuilder out = new StringBuilder();
        appendTree(out, "", 1);
        return out.toString();
    }

    /**
     * Appends one line for this component and then recurses into its children.
     *
     * @param out      the buffer being built
     * @param indent   the prefix for this level
     * @param quantity how many of this component the parent requires
     */
    protected void appendTree(StringBuilder out, String indent, int quantity) {
        out.append(indent)
                .append(quantity > 1 ? quantity + "x " : "")
                .append(name)
                .append(" [").append(partNumber).append("]")
                .append("  cost ").append(totalCost().times(quantity))
                .append(", ").append(totalWeightGrams() * quantity).append(" g")
                .append(System.lineSeparator());
        for (BomLine line : lines()) {
            line.component().appendTree(out, indent + "    ", line.quantity());
        }
    }
}
