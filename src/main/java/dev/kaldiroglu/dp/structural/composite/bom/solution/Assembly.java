package dev.kaldiroglu.dp.structural.composite.bom.solution;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite role of the Composite pattern — a sub-assembly or a finished
 * product.
 *
 * <p>An assembly has its own cost (labor, fasteners, paint — whatever is spent
 * putting it together) and a list of {@link BomLine}s naming what goes into it.
 * Each of the {@link BomComponent} queries is implemented by adding the
 * assembly's own contribution to the sum over its lines; the lines may point at
 * parts or at other assemblies, so the recursion descends as far as the product
 * structure goes.</p>
 *
 * <p>Three implementation issues from GoF's "Implementation" section (pp. 167–170)
 * show up here, and all three are driven by the same requirement — that the same
 * sub-assembly may be used in more than one place:</p>
 *
 * <ol>
 *   <li><b>Sharing components (p. 167).</b> Quantity lives on the {@link BomLine},
 *       so one {@code Assembly} instance serves every parent that needs it. Two
 *       wheels on a bicycle are one object referenced twice, not two objects.</li>
 *   <li><b>Caching to improve performance (p. 169).</b> Roll-ups are memoized,
 *       because a costing screen asks for the same total repeatedly and a deep
 *       product structure is expensive to walk.</li>
 *   <li><b>Explicit parent references (p. 167).</b> A cache must be invalidated
 *       when anything <em>below</em> it changes, which means a modified node has
 *       to reach its parents. Because components are shared, that reference is a
 *       <em>list</em> of parents, not a single one.</li>
 * </ol>
 */
public final class Assembly extends BomComponent {

    private final Money assemblyCost;
    private final int assemblyWeightGrams;
    private final List<BomLine> lines = new ArrayList<>();
    private final List<Assembly> parents = new ArrayList<>();

    // Memoized roll-ups; null means "not computed since the last change".
    private Money cachedCost;
    private Integer cachedWeightGrams;
    private Integer cachedPartCount;

    /**
     * Creates an assembly.
     *
     * @param partNumber          the catalog identifier
     * @param name                the human-readable name
     * @param assemblyCost        what it costs to put this level together,
     *                            excluding the components it contains
     * @param assemblyWeightGrams the mass this level adds itself, e.g. glue or
     *                            weld, excluding the components it contains
     */
    public Assembly(String partNumber, String name, Money assemblyCost, int assemblyWeightGrams) {
        super(partNumber, name);
        if (assemblyWeightGrams < 0) {
            throw new IllegalArgumentException("weight must not be negative");
        }
        this.assemblyCost = assemblyCost;
        this.assemblyWeightGrams = assemblyWeightGrams;
    }

    /** Creates an assembly that costs nothing extra to put together. */
    public Assembly(String partNumber, String name) {
        this(partNumber, name, Money.ZERO, 0);
    }

    /** Creates an assembly from the shared reference data. */
    public Assembly(Catalog.AssemblySpec spec) {
        this(spec.partNumber(), spec.name(), spec.assemblyCost(), spec.assemblyWeightGrams());
    }

    // --- Child management: declared on the Composite, not on the Component ---

    /**
     * Adds {@code quantity} of {@code component} to this assembly.
     *
     * @return this assembly, so lines can be chained while building a product
     * @throws IllegalArgumentException if the component is already a line here,
     *         or if adding it would make the structure cyclic
     */
    public Assembly add(BomComponent component, int quantity) {
        if (findLine(component) != null) {
            throw new IllegalArgumentException(
                    "component " + component.partNumber() + " is already a line of "
                            + partNumber() + "; change its quantity instead");
        }
        if (component == this || component.containsDeep(this)) {
            // Without this check a product could contain itself and every
            // roll-up would recurse forever.
            throw new IllegalArgumentException(
                    "adding " + component.partNumber() + " to " + partNumber()
                            + " would create a cycle in the product structure");
        }

        lines.add(new BomLine(component, quantity));
        if (component instanceof Assembly assembly) {
            assembly.parents.add(this);
        }
        invalidate();
        return this;
    }

    /** Adds exactly one of {@code component} to this assembly. */
    public Assembly add(BomComponent component) {
        return add(component, 1);
    }

    /**
     * Removes {@code component} from this assembly.
     *
     * @return {@code true} if a line was removed
     */
    public boolean remove(BomComponent component) {
        BomLine line = findLine(component);
        if (line == null) {
            return false;
        }
        lines.remove(line);
        if (component instanceof Assembly assembly) {
            assembly.parents.remove(this);
        }
        invalidate();
        return true;
    }

    /**
     * Changes the quantity of an existing line — the everyday engineering change.
     *
     * @throws IllegalArgumentException if the component is not a line here
     */
    public void changeQuantity(BomComponent component, int newQuantity) {
        BomLine line = findLine(component);
        if (line == null) {
            throw new IllegalArgumentException(
                    component.partNumber() + " is not a line of " + partNumber());
        }
        lines.set(lines.indexOf(line), new BomLine(component, newQuantity));
        invalidate();
    }

    // --- The roll-ups: own contribution plus the sum over the lines ----------

    @Override
    public Money totalCost() {
        if (cachedCost == null) {
            Money total = assemblyCost;
            for (BomLine line : lines) {
                total = total.plus(line.extendedCost()); // recurses through the line
            }
            cachedCost = total;
        }
        return cachedCost;
    }

    @Override
    public int totalWeightGrams() {
        if (cachedWeightGrams == null) {
            int total = assemblyWeightGrams;
            for (BomLine line : lines) {
                total += line.extendedWeightGrams();
            }
            cachedWeightGrams = total;
        }
        return cachedWeightGrams;
    }

    @Override
    public int partCount() {
        if (cachedPartCount == null) {
            int total = 0;
            for (BomLine line : lines) {
                total += line.extendedPartCount();
            }
            cachedPartCount = total;
        }
        return cachedPartCount;
    }

    @Override
    public List<BomLine> lines() {
        return List.copyOf(lines);
    }

    @Override
    public boolean isAssembly() {
        return true;
    }

    /** The cost of putting this level together, excluding its contents. */
    public Money assemblyCost() {
        return assemblyCost;
    }

    /** The assemblies that directly contain this one, in insertion order. */
    public List<Assembly> parents() {
        return List.copyOf(parents);
    }

    // --- Cache maintenance --------------------------------------------------

    /**
     * Discards this assembly's memoized roll-ups and those of every assembly
     * above it.
     *
     * <p>This is the price of caching in a Composite: a change deep in the tree
     * invalidates answers held higher up, so the change has to travel upwards.
     * The cycle check in {@link #add(BomComponent, int)} is what guarantees this
     * walk terminates.</p>
     */
    private void invalidate() {
        cachedCost = null;
        cachedWeightGrams = null;
        cachedPartCount = null;
        for (Assembly parent : parents) {
            parent.invalidate();
        }
    }

    private BomLine findLine(BomComponent component) {
        for (BomLine line : lines) {
            if (line.component() == component) {
                return line;
            }
        }
        return null;
    }

    // --- Package-private hooks so the tests can observe the caching ---------

    boolean isCostCached() {
        return cachedCost != null;
    }
}
