package dev.kaldiroglu.dp.behavioral.visitor.checkout.solution;

import dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.OverloadedTax;
import dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.TypeTestShipping;
import dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.TypeTestTax;
import dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Tax;

import java.util.List;

/**
 * Runs one cart through stage two, stage three, the visitors and the sealed switch.
 * <p>
 * The cart: a book for 40, food for 100, electronics for 500 — then a gift card for 100.
 */
public final class Main {

    public static void main(String[] args) {
        List<dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.Item> three = List.of(
                new dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.Book("Novel", 40),
                new dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.Food("Coffee", 100),
                new dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.Electronics("Headphones", 500));
        List<dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.Item> four = new java.util.ArrayList<>(three);
        four.add(new dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.GiftCard("Gift card", 100));

        System.out.println("Three items, correct tax 103");
        System.out.println("  overloads:  tax " + new OverloadedTax().total(three));
        System.out.println("  type tests: tax " + new TypeTestTax().total(three));

        System.out.println("A gift card is added, correct tax 103, correct shipping 50");
        System.out.println("  type tests: tax " + new TypeTestTax().total(four)
                + ", shipping " + new TypeTestShipping().total(four));

        Checkout checkout = new Checkout(List.of(
                new Book("Novel", 40),
                new Food("Coffee", 100),
                new Electronics("Headphones", 500),
                new GiftCard("Gift card", 100)));
        System.out.println("  visitors:   tax " + checkout.total(new TaxVisitor())
                + ", shipping " + checkout.total(new ShippingVisitor()));

        List<dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Item> sealed = List.of(
                new dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Book("Novel", 40),
                new dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Food("Coffee", 100),
                new dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Electronics("Headphones", 500),
                new dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.GiftCard("Gift card", 100));
        System.out.println("  switch:     tax " + Tax.total(sealed));

        System.out.println("Receipt");
        checkout.lines(new ReceiptLineVisitor()).forEach(line -> System.out.println("  " + line));
    }
}
