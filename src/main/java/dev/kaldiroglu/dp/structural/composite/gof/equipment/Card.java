package dev.kaldiroglu.dp.structural.composite.gof.equipment;

/**
 * Leaf role of the Composite solution (GoF, p. 172) — a simple piece of
 * equipment: an expansion card that plugs into a {@link Bus}.
 */
public class Card extends Equipment {

    private static final double DISCOUNT_RATE = 0.05; // 5% off list

    private final int watts;
    private final Currency listPrice;

    public Card(String name) {
        this(name, 8, Currency.of(120.00));
    }

    public Card(String name, int watts, Currency listPrice) {
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
