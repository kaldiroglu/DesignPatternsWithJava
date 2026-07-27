package dev.kaldiroglu.dp.structural.decorator.toast.solution;

/**
 * A ConcreteDecorator: adds Ketchup to whatever it is wrapped around.
 * <p>
 * The class knows its own name and its own price. That is the difference between a topping
 * and a parameter: repricing Ketchup is an edit here and nowhere else.
 */
public class Ketchup extends Topping {

    public Ketchup(Toastable component) {
        super(component, "Ketchup", 1);
    }
}
