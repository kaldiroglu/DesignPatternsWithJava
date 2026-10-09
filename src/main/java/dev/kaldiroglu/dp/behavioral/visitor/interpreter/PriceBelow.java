package dev.kaldiroglu.dp.behavioral.visitor.interpreter;

/** A terminal expression: the product costs less than this. */
public record PriceBelow(int limit) implements Rule {

    @Override
    public boolean interpret(Product product) {
        return product.price() < limit;
    }

    @Override
    public String describe() {
        return "price below " + limit;
    }
}
