package dev.kaldiroglu.dp.behavioral.visitor.checkout.solution;

/** A <b>ConcreteVisitor</b>: the whole shipping rule, in one class. */
public final class ShippingVisitor implements ItemVisitor<Integer> {

    @Override
    public Integer visit(Book book) {
        return Rates.BOOK_SHIPPING;
    }

    @Override
    public Integer visit(Food food) {
        return Rates.FOOD_SHIPPING;
    }

    @Override
    public Integer visit(Electronics electronics) {
        return Rates.ELECTRONICS_SHIPPING;
    }

    @Override
    public Integer visit(GiftCard giftCard) {
        return 0;                       // sent by e-mail
    }
}
