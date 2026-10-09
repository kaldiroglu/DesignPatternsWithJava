package dev.kaldiroglu.dp.behavioral.visitor.checkout.solution;

/** A <b>ConcreteVisitor</b> that returns text: the line printed on the receipt. */
public final class ReceiptLineVisitor implements ItemVisitor<String> {

    @Override
    public String visit(Book book) {
        return "Book        " + book.name() + " " + book.price();
    }

    @Override
    public String visit(Food food) {
        return "Food        " + food.name() + " " + food.price();
    }

    @Override
    public String visit(Electronics electronics) {
        return "Electronics " + electronics.name() + " " + electronics.price();
    }

    @Override
    public String visit(GiftCard giftCard) {
        return "Gift card   " + giftCard.name() + " " + giftCard.price() + " (sent by e-mail)";
    }
}
