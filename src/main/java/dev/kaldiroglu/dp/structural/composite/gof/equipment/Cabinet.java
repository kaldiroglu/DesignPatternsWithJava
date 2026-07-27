package dev.kaldiroglu.dp.structural.composite.gof.equipment;

/**
 * A concrete Composite (GoF, p. 172): the cabinet that holds chassis.
 *
 * <p>A cabinet is a passive enclosure — it consumes no power of its own — which
 * shows that a composite need not contribute anything to the aggregate. Its
 * whole job can be to hold children.</p>
 */
public class Cabinet extends CompositeEquipment {

    private final Currency listPrice;

    public Cabinet(String name) {
        this(name, Currency.of(90.00));
    }

    public Cabinet(String name, Currency listPrice) {
        super(name);
        this.listPrice = listPrice;
    }

    @Override
    protected Currency ownNetPrice() {
        return listPrice;
    }

    @Override
    protected double ownDiscountRate() {
        return 0.20; // 20% off list on the empty cabinet
    }
}
