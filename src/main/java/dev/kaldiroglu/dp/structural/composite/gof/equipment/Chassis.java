package dev.kaldiroglu.dp.structural.composite.gof.equipment;

/**
 * A concrete Composite (GoF, p. 172): the chassis that holds buses and drives.
 *
 * <p>The book's {@code Chassis::NetPrice} adds the chassis's own price to the
 * aggregate computed by {@code CompositeEquipment::NetPrice}. Here that is
 * expressed by overriding the {@code own...} hooks rather than the aggregate
 * operations, so the summing loop lives in exactly one place.</p>
 */
public class Chassis extends CompositeEquipment {

    private final int watts;
    private final Currency listPrice;

    public Chassis(String name) {
        this(name, 25, Currency.of(210.00));
    }

    public Chassis(String name, int watts, Currency listPrice) {
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
        return 0.15; // 15% off list on the chassis itself
    }
}
