package dev.kaldiroglu.dp.structural.decorator.toast.solution;

/**
 * A ConcreteDecorator: adds Cheddar cheese to whatever it is wrapped around.
 * <p>
 * The class knows its own name and its own price. That is the difference between a topping
 * and a parameter: repricing Cheddar cheese is an edit here and nowhere else.
 */
public class Cheese extends Topping {

    public Cheese(Toastable component) {
        super(component, "Cheddar cheese", 3);
    }
}
