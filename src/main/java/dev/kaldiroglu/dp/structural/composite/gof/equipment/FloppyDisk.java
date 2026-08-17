package dev.kaldiroglu.dp.structural.composite.gof.equipment;

/**
 * Leaf role of the Composite solution (GoF, p. 172) — a simple piece of
 * equipment.
 *
 * <p>A disk drive answers the {@link Equipment} operations from its own state:
 * there are no children to consult.</p>
 */
public class FloppyDisk extends Equipment {

    private static final double DISCOUNT_RATE = 0.10; // 10% off list

    private final int watts;
    private final Currency listPrice;

    public FloppyDisk(String name) {
        this(name, 15, Currency.of(35.00));
    }

    public FloppyDisk(String name, int watts, Currency listPrice) {
        super(name);
        this.watts = watts;
        this.listPrice = listPrice;
    }

    @Override
    public int power() {
        return watts;
    }

    @Override
    public Currency netPrice() {
        return listPrice;
    }

    @Override
    public Currency discountPrice() {
        return listPrice.times(1.0 - DISCOUNT_RATE);
    }
}
