package dev.kaldiroglu.dp.structural.decorator.toast.problem;

/**
 * The root of the menu.
 * <p>
 * Every toast the shop sells is a subclass of this class. That sentence sounds harmless, and
 * it is the whole problem: a toast is defined by the set of toppings on it, so "one class per
 * toast" turns into "one class per <em>combination</em> of toppings".
 */
public abstract class AbstractToast implements Toast {

    protected String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " = " + calculatePrice();
    }
}
