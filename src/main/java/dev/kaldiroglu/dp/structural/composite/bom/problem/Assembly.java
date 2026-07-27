package dev.kaldiroglu.dp.structural.composite.bom.problem;

import dev.kaldiroglu.dp.structural.composite.bom.domain.Catalog;
import dev.kaldiroglu.dp.structural.composite.bom.domain.Money;

import java.util.ArrayList;
import java.util.List;

/**
 * The naive design's assembly — a plain class with <b>two separate child
 * collections</b> and no supertype in common with {@link Part}.
 *
 * <p>This is the design most developers reach for first, and it is worth
 * understanding <em>why</em> it is reached for: it is a direct transcription of
 * the sentence "an assembly contains parts and sub-assemblies". The trouble is
 * that the sentence is a description of the data, not of the behavior, and the
 * behavior is where the cost lands.</p>
 *
 * <p>Four consequences follow, and every one of them is visible in this file or
 * in the clients that use it:</p>
 *
 * <ol>
 *   <li><b>Two collections must be kept in step.</b> Every traversal, in every
 *       client, has to remember to visit both. Forget one and the answer is
 *       silently wrong.</li>
 *   <li><b>There is nowhere for a quantity to live.</b> Thirty-two spokes means
 *       thirty-two {@code Part} objects in {@link #parts()}; two wheels means two
 *       separately built {@code Assembly} objects in {@link #subAssemblies()}.</li>
 *   <li><b>Therefore nothing can be shared.</b> The two wheels of a bicycle are
 *       two <em>different</em> objects that merely happen to have been built the
 *       same way, and they can drift apart without anyone noticing — see
 *       {@code NaiveBomTest.theTwoWheelsAreDifferentObjectsAndCanDriftApart}.</li>
 *   <li><b>No operation can live here.</b> There is no type that spans parts and
 *       assemblies, so {@code totalCost} cannot be a method on the thing being
 *       costed. It has to become a static function somewhere else — see
 *       {@link NaiveCosting} — and it has to branch on type.</li>
 * </ol>
 *
 * <p>Compare with {@code solution.Assembly}, which has <em>one</em> child
 * collection of {@code BomLine}s and carries its own roll-up operations.</p>
 */
public class Assembly {

    private final String partNumber;
    private final String name;
    private final Money assemblyCost;
    private final int assemblyWeightGrams;

    // Two collections, because there is no type that covers both.
    private final List<Part> parts = new ArrayList<>();
    private final List<Assembly> subAssemblies = new ArrayList<>();

    public Assembly(String partNumber, String name, Money assemblyCost, int assemblyWeightGrams) {
        this.partNumber = partNumber;
        this.name = name;
        this.assemblyCost = assemblyCost;
        this.assemblyWeightGrams = assemblyWeightGrams;
    }

    /** Creates an assembly from the shared reference data. */
    public Assembly(Catalog.AssemblySpec spec) {
        this(spec.partNumber(), spec.name(), spec.assemblyCost(), spec.assemblyWeightGrams());
    }

    public String partNumber() {
        return partNumber;
    }

    public String name() {
        return name;
    }

    public Money assemblyCost() {
        return assemblyCost;
    }

    public int assemblyWeightGrams() {
        return assemblyWeightGrams;
    }

    /**
     * The purchased parts, as a <b>live, mutable list</b>.
     *
     * <p>Returning the live list is itself a symptom. Because the operations that
     * matter live outside this class, the class has no way to react to a change —
     * so there is no point in defending the collection, and callers end up
     * writing {@code assembly.parts().add(...)}.</p>
     */
    public List<Part> parts() {
        return parts;
    }

    /** The nested assemblies, as a live, mutable list, for the same reason. */
    public List<Assembly> subAssemblies() {
        return subAssemblies;
    }

    /** Adds a part. A convenience over {@code parts().add(part)}, nothing more. */
    public void addPart(Part part) {
        parts.add(part);
    }

    /** Adds a part {@code quantity} times, because quantity has nowhere else to live. */
    public void addPart(Part part, int quantity) {
        for (int i = 0; i < quantity; i++) {
            parts.add(part);
        }
    }

    /** Adds a nested assembly. */
    public void addSubAssembly(Assembly subAssembly) {
        subAssemblies.add(subAssembly);
    }
}
