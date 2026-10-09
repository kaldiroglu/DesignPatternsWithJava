package dev.kaldiroglu.dp.behavioral.visitor.checkout;

import dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.visitor.Printed.codeOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The three designs of Part 1: each item computes its own tax, one class with an overload
 * per kind, and one class with type tests. Every figure the Part 1 slides quote is asserted
 * here.
 */
class ProblemTest {

    private static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/behavioral/visitor/checkout/problem/";

    /** A book for 40, food for 100 and electronics for 500. */
    static List<Item> threeItems() {
        return List.of(new Book("Novel", 40), new Food("Coffee", 100),
                new Electronics("Headphones", 500));
    }

    /** The same cart, and a gift card for 100. */
    static List<Item> withGiftCard() {
        List<Item> cart = new ArrayList<>(threeItems());
        cart.add(new GiftCard("Gift card", 100));
        return List.copyOf(cart);
    }

    @Test
    @DisplayName("the rates are books 5%, food 1% and electronics 20%")
    void theRates() {
        assertEquals(5, Rates.BOOK_TAX);
        assertEquals(1, Rates.FOOD_TAX);
        assertEquals(20, Rates.ELECTRONICS_TAX);
        assertEquals(20, Rates.STANDARD_TAX);
    }

    @Test
    @DisplayName("stage one: each item computes its own tax, 2 + 1 + 100 = 103")
    void eachItemComputesItsOwnTax() {
        var book = new dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.methods.Book("Novel", 40);
        var food = new dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.methods.Food("Coffee", 100);
        var electronics = new dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.methods.Electronics("Headphones", 500);

        assertEquals(2, book.tax());
        assertEquals(1, food.tax());
        assertEquals(100, electronics.tax());
        assertEquals(103, book.tax() + food.tax() + electronics.tax());
        assertEquals(50, book.shippingCost() + food.shippingCost() + electronics.shippingCost());
        assertEquals("Novel 40 (tax 2)", book.receiptLine());
    }

    @Test
    @DisplayName("stage one: every operation is a method on every item, three operations in three classes")
    void everyOperationOnEveryItem() {
        List<String> operations = Arrays.stream(
                        dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.methods.Item.class.getDeclaredMethods())
                .map(Method::getName)
                .filter(name -> !name.equals("name") && !name.equals("price"))
                .sorted()
                .toList();
        assertEquals(List.of("receiptLine", "shippingCost", "tax"), operations);
    }

    @Test
    @DisplayName("stage two: overloads are chosen at compile time, so the cart's tax is 128 and not 103")
    void overloadsChargeTheStandardRate() {
        OverloadedTax tax = new OverloadedTax();

        assertEquals(128, tax.total(threeItems()));
        // Called with the exact static type, each overload is correct. The loop never does that.
        assertEquals(2, tax.taxOf(new Book("Novel", 40)));
        assertEquals(1, tax.taxOf(new Food("Coffee", 100)));
        Item book = new Book("Novel", 40);
        assertEquals(8, tax.taxOf(book), "a book held as an Item is taxed at 20 percent");
    }

    @Test
    @DisplayName("stage three: type tests give the correct tax, 103, for the three items")
    void typeTestsAreCorrectForKnownItems() {
        assertEquals(103, new TypeTestTax().total(threeItems()));
        assertEquals(50, new TypeTestShipping().total(threeItems()));
    }

    @Test
    @DisplayName("stage three: a gift card falls into the else branch, so tax is 123 and shipping 60")
    void theGiftCardFallsIntoTheElse() {
        TypeTestTax tax = new TypeTestTax();

        assertEquals(List.of(2, 1, 100, 20), withGiftCard().stream().map(tax::taxOf).toList());
        assertEquals(123, tax.total(withGiftCard()));
        assertEquals(60, new TypeTestShipping().total(withGiftCard()));
    }

    @Test
    @DisplayName("stage three's Item is not sealed, so the compiler cannot list the kinds of item")
    void theItemIsNotSealed() {
        assertFalse(Item.class.isSealed());
        String code = codeOf(SOURCE + "TypeTestTax.java");
        assertEquals(3, code.split("instanceof", -1).length - 1);
        assertTrue(code.contains("else {"));
    }
}
