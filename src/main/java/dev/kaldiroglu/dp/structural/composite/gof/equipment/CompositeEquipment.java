package dev.kaldiroglu.dp.structural.composite.gof.equipment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Composite role of the Composite solution — the {@code CompositeEquipment}
 * class of the GoF Sample Code (GoF, p. 171).
 *
 * <p>The base class for equipment that <em>contains</em> other equipment. It
 * holds the child list and implements each {@link Equipment} operation by
 * iterating over the children and combining their answers. Concrete assemblies
 * ({@link Chassis}, {@link Cabinet}, {@link Bus}) inherit that behavior and add
 * their own contribution on top of it.</p>
 *
 * <p>This is where the solution earns its keep. Note that
 * {@link #netPrice()} does not care whether a child is a card or another whole
 * chassis — the recursion terminates by itself when it reaches leaves.</p>
 */
public abstract class CompositeEquipment extends Equipment {

    private final List<Equipment> equipment = new ArrayList<>();

    protected CompositeEquipment(String name) {
        super(name);
    }

    /**
     * The power drawn by the assembly's own circuitry, excluding its contents.
     * Subclasses override it; by default an enclosure consumes nothing itself.
     */
    protected int ownPower() {
        return 0;
    }

    /** The price of the assembly's own hardware, excluding its contents. */
    protected Currency ownNetPrice() {
        return Currency.ZERO;
    }

    /** The discount rate applied to the assembly's own hardware. */
    protected double ownDiscountRate() {
        return 0.0;
    }

    @Override
    public int power() {
        int total = ownPower();
        for (Equipment part : equipment) {
            total += part.power(); // may recurse into another assembly
        }
        return total;
    }

    @Override
    public Currency netPrice() {
        Currency total = ownNetPrice();
        for (Equipment part : equipment) {
            total = total.plus(part.netPrice());
        }
        return total;
    }

    @Override
    public Currency discountPrice() {
        Currency total = ownNetPrice().times(1.0 - ownDiscountRate());
        for (Equipment part : equipment) {
            total = total.plus(part.discountPrice());
        }
        return total;
    }

    @Override
    public void add(Equipment part) {
        equipment.add(part);
    }

    @Override
    public void remove(Equipment part) {
        equipment.remove(part);
    }

    @Override
    public Iterator<Equipment> iterator() {
        return List.copyOf(equipment).iterator();
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    /** The directly contained equipment, as an unmodifiable list. */
    public List<Equipment> parts() {
        return List.copyOf(equipment);
    }
}
