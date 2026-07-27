package dev.kaldiroglu.dp.structural.decorator.toast.solution;

import java.util.List;
import java.util.Objects;

/**
 * A decorator that is not a topping.
 * <p>
 * Every {@link Topping} so far answers {@code calculatePrice()} by adding a number. A
 * decorator is not restricted to that: it may change the answer it gets back, ignore it, or
 * ask for it more than once. {@code Promotion} multiplies, so it shows that decoration is
 * about <em>behavior</em> and not about accumulation.
 * <p>
 * It implements {@link Toastable} directly rather than extending {@code Topping}, because it
 * is not something the customer eats and should not appear in {@link #getToppings()}. Sharing
 * the Component interface is all that is required to join the chain: a decorator needs no
 * common base class with the other decorators, only a common type with what it wraps.
 * <p>
 * Where it sits in the chain changes the bill, and by a different amount rather than a
 * different order of the same amount. {@code Promotion} outermost discounts the whole toast;
 * {@code Promotion} next to the bread discounts only the bread.
 */
public class Promotion implements Toastable {

    private final Toastable component;
    private final String description;
    private final int percentageOff;

    public Promotion(Toastable component, String description, int percentageOff) {
        this.component = Objects.requireNonNull(component);
        this.description = description;
        this.percentageOff = percentageOff;
    }

    @Override
    public int calculatePrice() {
        return component.calculatePrice() * (100 - percentageOff) / 100;
    }

    @Override
    public List<Topping> getToppings() {
        return component.getToppings(); // a discount is not a topping, so it adds nothing here
    }

    @Override
    public String toString() {
        return "Promotion [" + description + ", " + percentageOff + "% off]";
    }
}
