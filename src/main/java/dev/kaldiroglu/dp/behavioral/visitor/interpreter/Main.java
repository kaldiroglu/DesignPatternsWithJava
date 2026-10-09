package dev.kaldiroglu.dp.behavioral.visitor.interpreter;

import java.util.List;

/**
 * Builds one discount rule as a tree and interprets it against four products.
 * <p>
 * The tree is built by hand. Turning the text of a rule into this tree is parsing, and GoF
 * say the pattern does not cover it.
 */
public final class Main {

    public static void main(String[] args) {
        Rule discount = new Or(
                new And(new CategoryIs("books"), new PriceBelow(50)),
                new And(new CategoryIs("food"), new Not(new PriceBelow(20))));
        System.out.println("Rule: " + discount.describe());

        List<Product> products = List.of(
                new Product("Novel", "books", 40),
                new Product("Atlas", "books", 80),
                new Product("Coffee", "food", 30),
                new Product("Tea", "food", 10));
        for (Product product : products) {
            System.out.println("  " + product.name() + ", " + product.category() + ", "
                    + product.price() + ": " + (discount.interpret(product) ? "discount" : "no discount"));
        }
    }
}
