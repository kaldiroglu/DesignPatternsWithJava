package dev.kaldiroglu.dp.structural.decorator.toast.solution;

import java.util.List;

/**
 * The Component role: everything a toast can do, whether or not anything has been added to it.
 * <p>
 * This is the interface that makes decoration possible. A bare slice of bread and a slice
 * buried under five toppings are the same type here, so a decorator can wrap either one and
 * the code holding the result cannot tell which it has.
 */
public interface Toastable {

    int calculatePrice();

    /** The toppings on this toast, outermost last. Bare bread has none. */
    List<Topping> getToppings();
}
