package dev.kaldiroglu.dp.structural.decorator.toast.solution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The point of this class.
 * <p>
 * Five topping classes do the work of thirty-one combination classes, and the last three tests
 * assert things the problem package cannot express at all.
 */
class ToastSolutionTest {

    private static Toastable ayvalikToast() {
        return new Salad(new Ketchup(new Tomato(new Sausage(new Cheese(new ToastBread())))));
    }

    @Test
    @DisplayName("a topping is a Toastable, so a decorated toast goes wherever a plain one goes")
    void transparency() {
        Toastable bread = new ToastBread();
        Toastable topped = new Cheese(bread);

        assertInstanceOf(Toastable.class, bread);
        assertInstanceOf(Toastable.class, topped);

        // The decorator is both: it implements the interface and holds one.
        assertInstanceOf(Toastable.class, ((Topping) topped).getComponent());
    }

    @Test
    @DisplayName("the price is the sum of the chain, computed by forwarding")
    void priceAccumulatesThroughTheChain() {
        assertEquals(5, new ToastBread().calculatePrice());
        assertEquals(16, ayvalikToast().calculatePrice());
        assertEquals(List.of(3, 3, 2, 1, 2),
                ayvalikToast().getToppings().stream().map(Topping::getPrice).toList());
    }

    @Test
    @DisplayName("toppings come back innermost first")
    void toppingOrderIsReported() {
        assertEquals(List.of("Cheddar cheese", "Sucuk sausage", "Tomato", "Ketchup", "Russian salad"),
                ayvalikToast().getToppings().stream().map(Topping::getName).toList());
    }

    @Test
    @DisplayName("getToppings hands back a fresh list, so a caller cannot corrupt the toast")
    void getToppingsDoesNotShareItsList() {
        Toastable toast = ayvalikToast();

        List<Topping> first = toast.getToppings();
        first.clear();

        assertEquals(5, toast.getToppings().size());
        assertNotSame(first, toast.getToppings());
    }

    @Test
    @DisplayName("the decorator keeps no collection of its own")
    void theDecoratorHoldsOnlyItsComponent() {
        // A regression test with a history. An earlier version kept a List field and added
        // 'this' to it inside the constructor, which javac reports as a possible 'this' escape
        // before the subclass is initialized. The field was never read, because every use site
        // declared a local variable of the same name that shadowed it.
        for (Field field : Topping.class.getDeclaredFields()) {
            assertTrue(!List.class.isAssignableFrom(field.getType()),
                    "Topping should not hold a collection: " + field.getName());
        }
    }

    @Test
    @DisplayName("the same topping can be applied twice, which no combination class allows")
    void theSameToppingTwice() {
        assertEquals(11, new Cheese(new Cheese(new ToastBread())).calculatePrice());
        assertEquals(14, new Cheese(new Cheese(new Cheese(new ToastBread()))).calculatePrice());
    }

    @Test
    @DisplayName("a decorator may multiply rather than add, and its position changes the bill")
    void aDecoratorNeedNotJustAdd() {
        Toastable outside = new Promotion(ayvalikToast(), "student discount", 25);
        Toastable inside = new Salad(new Ketchup(new Tomato(new Sausage(
                new Cheese(new Promotion(new ToastBread(), "student discount", 25))))));

        assertEquals(12, outside.calculatePrice()); // 25% off the whole 16
        assertEquals(14, inside.calculatePrice());  // 25% off the bread alone, then toppings

        // Same decorators, same settings, different chain, different program.
        assertNotEquals(outside.calculatePrice(), inside.calculatePrice());
    }

    @Test
    @DisplayName("a promotion joins the chain without being a topping")
    void promotionIsNotATopping() {
        Toastable discounted = new Promotion(ayvalikToast(), "student discount", 25);

        // The discount is in the chain and changed the price ...
        assertEquals(12, discounted.calculatePrice());

        // ... but the customer is still eating exactly five toppings.
        assertEquals(5, discounted.getToppings().size());

        // Writing `topping instanceof Promotion` here does not compile: Promotion implements
        // Toastable directly rather than extending Topping, so the compiler can prove no
        // element of this list is ever one. The guarantee is stronger than a passing test.
        assertFalse(Topping.class.isAssignableFrom(Promotion.class));
        assertTrue(Toastable.class.isAssignableFrom(Promotion.class));
    }

    @Test
    @DisplayName("a topping must be added to something")
    void nullComponentIsRejected() {
        assertThrows(NullPointerException.class, () -> new Cheese(null));
    }

    @Test
    @DisplayName("each ToastBread keeps its own name")
    void breadNamesAreNotShared() {
        // Another regression test. The name field used to be static, so every slice of bread
        // in the program shared one name and the last one constructed won.
        ToastBread first = new ToastBread("Sourdough", 6);
        ToastBread second = new ToastBread("Rye", 7);

        assertEquals("Sourdough", first.getName());
        assertEquals("Rye", second.getName());
        assertEquals(6, first.calculatePrice());
        assertEquals(7, second.calculatePrice());
    }

    @Test
    @DisplayName("five topping classes replace thirty-one combination classes")
    void fiveClassesInsteadOfThirtyOne() {
        List<Class<?>> toppings = List.of(
                Cheese.class, Sausage.class, Tomato.class, Ketchup.class, Salad.class);

        assertEquals(5, toppings.size());
        assertEquals(31, (1 << toppings.size()) - 1);
        assertTrue(toppings.stream().allMatch(Topping.class::isAssignableFrom));
    }
}
