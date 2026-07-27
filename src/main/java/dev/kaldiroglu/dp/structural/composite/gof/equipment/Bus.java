package dev.kaldiroglu.dp.structural.composite.gof.equipment;

/**
 * A concrete Composite (GoF, p. 172): the bus that holds expansion cards.
 *
 * <p>A {@code Bus} is a composite that is itself contained by a
 * {@link Chassis}. It is the middle level of the example's tree, and the clearest
 * illustration that "composite" is a role an object plays, not a place in a
 * hierarchy of classes: the same object is a child to its parent and a parent to
 * its children.</p>
 */
public class Bus extends CompositeEquipment {

    private final int watts;
    private final Currency listPrice;

    public Bus(String name) {
        this(name, 10, Currency.of(75.00));
    }

    public Bus(String name, int watts, Currency listPrice) {
        super(name);
        this.watts = watts;
        this.listPrice = listPrice;
    }

    @Override
    protected int ownPower() {
        return watts;
    }

    @Override
    protected Currency ownNetPrice() {
        return listPrice;
    }

    @Override
    protected double ownDiscountRate() {
        return 0.10; // 10% off list on the bus itself
    }
}
