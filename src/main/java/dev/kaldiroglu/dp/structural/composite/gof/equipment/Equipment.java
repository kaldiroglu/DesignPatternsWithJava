package dev.kaldiroglu.dp.structural.composite.gof.equipment;

import java.util.Collections;
import java.util.Iterator;

/**
 * Component role of the Composite solution — the {@code Equipment} class of the
 * GoF Sample Code (GoF, "Design Patterns", p. 170).
 *
 * <p>Computer equipment such as a disk drive can be assembled into larger
 * units: a bus holds cards, a chassis holds buses and drives, a cabinet holds
 * chassis. This class is the common abstraction for both the simple pieces and
 * the assemblies, so a price or power query can be answered for a single card
 * and for a whole cabinet with the same call.</p>
 *
 * <p>The book's operations are all here: {@link #power()}, {@link #netPrice()},
 * {@link #discountPrice()}, the child operations {@link #add(Equipment)} /
 * {@link #remove(Equipment)}, and {@code CreateIterator} — expressed in Java by
 * implementing {@link Iterable}, which is the language's own Iterator solution
 * (GoF, p. 257; see the "Related Patterns" note that Iterator is used to
 * traverse composites).</p>
 */
public abstract class Equipment implements Iterable<Equipment> {

    private final String name;

    protected Equipment(String name) {
        this.name = name;
    }

    /** The equipment's name, e.g. "3.5in Floppy". */
    public String name() {
        return name;
    }

    /** Power consumption in watts. */
    public abstract int power();

    /** The list price of this piece of equipment. */
    public abstract Currency netPrice();

    /** The price actually charged, after the applicable discount. */
    public abstract Currency discountPrice();

    // --- Child management, declared in the Component (GoF, p. 168) -----------

    /**
     * Adds a piece of equipment to this assembly.
     *
     * @throws UnsupportedOperationException by default: a simple piece of
     *         equipment is not an assembly. {@link CompositeEquipment}
     *         overrides this.
     */
    public void add(Equipment part) {
        throw new UnsupportedOperationException(
                name + " is not an assembly and cannot contain other equipment");
    }

    /**
     * Removes a piece of equipment from this assembly.
     *
     * @throws UnsupportedOperationException by default, for the same reason as
     *         {@link #add(Equipment)}.
     */
    public void remove(Equipment part) {
        throw new UnsupportedOperationException(
                name + " is not an assembly and cannot contain other equipment");
    }

    /**
     * Iterates over the contained equipment — empty for a simple piece.
     *
     * <p>An empty iterator is the right default: it lets a client walk any
     * {@code Equipment} uniformly without asking what kind it is.</p>
     */
    @Override
    public Iterator<Equipment> iterator() {
        return Collections.emptyIterator();
    }

    /** Answers whether this equipment is an assembly that can hold parts. */
    public boolean isComposite() {
        return false;
    }
}
