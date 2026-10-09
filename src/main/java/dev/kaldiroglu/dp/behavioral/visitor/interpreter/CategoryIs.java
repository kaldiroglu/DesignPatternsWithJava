package dev.kaldiroglu.dp.behavioral.visitor.interpreter;

/** A terminal expression: the product is in this category. */
public record CategoryIs(String category) implements Rule {

    @Override
    public boolean interpret(Product product) {
        return product.category().equals(category);
    }

    @Override
    public String describe() {
        return "category is " + category;
    }
}
