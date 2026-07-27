package dev.kaldiroglu.dp.structural.decorator.toast.problem;

/**
 * The class that ends the argument.
 * <p>
 * A customer asks for cheese <em>and</em> sausage. The obvious implementation is to inherit
 * the cheese from {@link CheeseToast} and the sausage from {@link SausageToast}:
 *
 * <pre>{@code
 * class CheeseSausageToast extends CheeseToast, SausageToast   // does not compile
 * }</pre>
 *
 * Java allows one superclass, so this is not merely awkward, it is <em>impossible</em>. The
 * only way forward is the one taken below: extend {@link AbstractToast} and copy the numbers
 * out of both intended parents by hand.
 * <p>
 * Every further combination repeats this. With the five toppings the solution package offers
 * (cheese, sausage, tomato, ketchup and salad), a class per combination is
 * 2^5 - 1 = <strong>31</strong> classes, and most of them cannot share code with any of their
 * neighbours.
 */
public class CheeseSausageToast extends AbstractToast {

    public CheeseSausageToast() {
        name = "Cheese sausage toast";
    }

    @Override
    public int calculatePrice() {
        // Copied out of CheeseToast (5) and SausageToast (6), less the bread that would
        // otherwise be charged for twice. The subtraction is a guess, because no class in
        // this package ever says what the bread costs on its own.
        return 5 + 6 - 3;
    }
}
