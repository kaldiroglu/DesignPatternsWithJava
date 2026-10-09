package dev.kaldiroglu.dp.behavioral.visitor.checkout;

import dev.kaldiroglu.dp.behavioral.visitor.Printed;
import dev.kaldiroglu.dp.behavioral.visitor.checkout.solution.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.visitor.Printed.codeOf;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The checkout with visitors, and the same check with a sealed interface and a switch.
 * Every figure the Part 3 slides quote about them is asserted here.
 */
class SolutionTest {

    private static final String SOURCE =
            "src/main/java/dev/kaldiroglu/dp/behavioral/visitor/checkout/";

    private static final List<Class<?>> ITEMS =
            List.of(Book.class, Food.class, Electronics.class, GiftCard.class);

    private static Checkout threeItems() {
        return new Checkout(List.of(new Book("Novel", 40), new Food("Coffee", 100),
                new Electronics("Headphones", 500)));
    }

    private static Checkout withGiftCard() {
        return new Checkout(List.of(new Book("Novel", 40), new Food("Coffee", 100),
                new Electronics("Headphones", 500), new GiftCard("Gift card", 100)));
    }

    @Test
    @DisplayName("visitors give tax 103 for three items, and still 103 with a gift card")
    void theTax() {
        assertEquals(103, threeItems().total(new TaxVisitor()));
        assertEquals(103, withGiftCard().total(new TaxVisitor()));
        assertEquals(0, new GiftCard("Gift card", 100).accept(new TaxVisitor()));
    }

    @Test
    @DisplayName("visitors give shipping 50 with a gift card, because a gift card is not shipped")
    void theShipping() {
        assertEquals(50, threeItems().total(new ShippingVisitor()));
        assertEquals(50, withGiftCard().total(new ShippingVisitor()));
    }

    @Test
    @DisplayName("one visitor returns numbers and another returns text, over the same items")
    void aVisitorReturnsWhatItNeeds() {
        assertEquals(List.of(
                "Book        Novel 40",
                "Food        Coffee 100",
                "Electronics Headphones 500",
                "Gift card   Gift card 100 (sent by e-mail)"), withGiftCard().lines(new ReceiptLineVisitor()));
    }

    @Test
    @DisplayName("accept calls the visit method for the item's own class")
    void doubleDispatch() {
        ItemVisitor<String> names = new ItemVisitor<>() {
            public String visit(Book book) { return "book"; }
            public String visit(Food food) { return "food"; }
            public String visit(Electronics electronics) { return "electronics"; }
            public String visit(GiftCard giftCard) { return "gift card"; }
        };
        List<Item> cart = List.of(new Book("a", 1), new Food("b", 1), new Electronics("c", 1),
                new GiftCard("d", 1));

        assertEquals(List.of("book", "food", "electronics", "gift card"),
                cart.stream().map(item -> item.accept(names)).toList());
    }

    @Test
    @DisplayName("ItemVisitor declares one abstract visit method for each of the four items, so a visitor without visit(GiftCard) does not compile")
    void oneVisitPerItem() {
        List<Method> visits = Arrays.stream(ItemVisitor.class.getDeclaredMethods()).toList();

        assertEquals(4, visits.size());
        assertTrue(visits.stream().allMatch(m -> m.getName().equals("visit")));
        assertTrue(visits.stream().allMatch(m -> Modifier.isAbstract(m.getModifiers())),
                "no default method, so every visitor must write each one");
        assertEquals(ITEMS, visits.stream()
                .<Class<?>>map(m -> m.getParameterTypes()[0])
                .sorted((a, b) -> ITEMS.indexOf(a) - ITEMS.indexOf(b))
                .toList());
    }

    @Test
    @DisplayName("every concrete visitor handles all four items")
    void everyVisitorIsFinished() {
        for (Class<?> visitor : List.of(TaxVisitor.class, ShippingVisitor.class, ReceiptLineVisitor.class)) {
            for (Class<?> item : ITEMS) {
                assertDoesNotThrow(() -> visitor.getDeclaredMethod("visit", item),
                        visitor.getSimpleName() + " visits " + item.getSimpleName());
            }
        }
    }

    @Test
    @DisplayName("the visitors have no instanceof and no casts")
    void noTypeTests() {
        for (String name : List.of("TaxVisitor", "ShippingVisitor", "ReceiptLineVisitor", "Checkout")) {
            String code = codeOf(SOURCE + "solution/" + name + ".java");
            assertFalse(code.contains("instanceof"), name);
            assertFalse(code.contains("default"), name);
        }
    }

    @Test
    @DisplayName("the modern Item is sealed and permits exactly four records")
    void theSealedItem() {
        Class<?> item = dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Item.class;
        assertTrue(item.isSealed());
        List<Class<?>> permitted = List.of(item.getPermittedSubclasses());

        assertEquals(List.of(
                dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Book.class,
                dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Food.class,
                dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Electronics.class,
                dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.GiftCard.class), permitted);
        assertTrue(permitted.stream().allMatch(Class::isRecord));
    }

    @Test
    @DisplayName("the switch has a case for each of the four records and no default, and the tax is 103")
    void theSwitch() {
        String code = codeOf(SOURCE + "modern/Tax.java");
        assertEquals(4, code.split("case ", -1).length - 1);
        assertFalse(code.contains("default"));

        List<dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Item> cart = List.of(
                new dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Book("Novel", 40),
                new dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Food("Coffee", 100),
                new dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Electronics("Headphones", 500),
                new dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.GiftCard("Gift card", 100));
        assertEquals(103, dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Tax.total(cart));
        assertEquals(103, dev.kaldiroglu.dp.behavioral.visitor.checkout.modern.Tax.total(cart.subList(0, 3)));
    }

    @Test
    @DisplayName("Main prints 128, 103, 123 and 60, then 103 and 50 for visitors and 103 for the switch")
    void mainOutput() {
        List<String> lines = Printed.by(() -> Main.main(new String[0]));

        assertEquals(List.of(
                "Three items, correct tax 103",
                "  overloads:  tax 128",
                "  type tests: tax 103",
                "A gift card is added, correct tax 103, correct shipping 50",
                "  type tests: tax 123, shipping 60",
                "  visitors:   tax 103, shipping 50",
                "  switch:     tax 103",
                "Receipt",
                "  Book        Novel 40",
                "  Food        Coffee 100",
                "  Electronics Headphones 500",
                "  Gift card   Gift card 100 (sent by e-mail)"), lines);
    }
}
