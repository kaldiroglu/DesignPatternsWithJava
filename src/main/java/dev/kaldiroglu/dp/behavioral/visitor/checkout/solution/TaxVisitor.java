package dev.kaldiroglu.dp.behavioral.visitor.checkout.solution;

/** A <b>ConcreteVisitor</b>: the whole tax rule, in one class. */
public final class TaxVisitor implements ItemVisitor<Integer> {

    @Override
    public Integer visit(Book book) {
        return book.price() * Rates.BOOK_TAX / 100;
    }

    @Override
    public Integer visit(Food food) {
        return food.price() * Rates.FOOD_TAX / 100;
    }

    @Override
    public Integer visit(Electronics electronics) {
        return electronics.price() * Rates.ELECTRONICS_TAX / 100;
    }

    @Override
    public Integer visit(GiftCard giftCard) {
        return 0;                       // taxed when it is spent
    }
}
