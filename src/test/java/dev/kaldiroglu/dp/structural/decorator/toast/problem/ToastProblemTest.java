package dev.kaldiroglu.dp.structural.decorator.toast.problem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * The point of this class.
 * <p>
 * The naive design is not broken: every price it computes is correct. These tests measure what
 * it costs instead, so the argument for the solution rests on numbers rather than on taste.
 */
class ToastProblemTest {

    @Test
    @DisplayName("the menu prices are all correct, which is exactly why the design survives review")
    void pricesAreCorrect() {
        assertEquals(5, new CheeseToast().calculatePrice());
        assertEquals(7, new CheeseTomatoToast().calculatePrice());
        assertEquals(6, new SausageToast().calculatePrice());
        assertEquals(8, new SausageTomatoToast().calculatePrice());
        assertEquals(8, new CheeseSausageToast().calculatePrice());
    }

    @Test
    @DisplayName("the price of tomato is stored twice, in two classes that cannot share it")
    void theTomatoPriceIsScattered() {
        int tomatoOnCheese = new CheeseTomatoToast().calculatePrice() - new CheeseToast().calculatePrice();
        int tomatoOnSausage = new SausageTomatoToast().calculatePrice() - new SausageToast().calculatePrice();

        assertEquals(2, tomatoOnCheese);
        assertEquals(2, tomatoOnSausage);

        // The same 2, written out in two unrelated subclasses. Nothing in the type system ties
        // them together, so repricing tomato is a search-and-hope operation.
        assertEquals(tomatoOnCheese, tomatoOnSausage);
    }

    @Test
    @DisplayName("cheese and sausage together cannot inherit from both, so the prices were copied")
    void combiningTwoToppingsForcesDuplication() {
        // The class the shop wanted was "extends CheeseToast, SausageToast". Java permits one
        // superclass, so what it got was a sibling of both, sharing code with neither.
        assertSame(AbstractToast.class, CheeseSausageToast.class.getSuperclass());
        assertFalse(CheeseToast.class.isAssignableFrom(CheeseSausageToast.class));
        assertFalse(SausageToast.class.isAssignableFrom(CheeseSausageToast.class));
    }

    @Test
    @DisplayName("a class per combination of five toppings is thirty-one classes")
    void theCombinatorialCount() {
        // Each topping is independently on or off, so the subsets of five toppings number
        // 2^5 = 32, of which the empty one is the bare bread.
        assertEquals(32, 1 << 5);
        assertEquals(31, (1 << 5) - 1);

        // Written out: five singles, ten pairs, ten triples, five quadruples, one full house.
        assertEquals(31, 5 + 10 + 10 + 5 + 1);
    }

    @Test
    @DisplayName("toppings are fixed by type, so nothing can be added after construction")
    void toppingsAreFrozenAtConstruction() {
        Toast ordered = new CheeseToast();

        // There is no method to call here. Adding tomato means becoming a CheeseTomatoToast,
        // and an object cannot change its class. The only route is to throw the order away
        // and construct a different one, which is what the solution removes the need for.
        assertEquals(5, ordered.calculatePrice());
        assertEquals(1, ordered.getClass().getMethods().length
                - Object.class.getMethods().length
                - 1 /* getName */);
    }
}
