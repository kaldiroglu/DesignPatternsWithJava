package dev.kaldiroglu.dp.structural.decorator.toast.solution;

/**
 * A ConcreteDecorator: adds Sucuk to whatever it is wrapped around.
 * <p>
 * The class knows its own name and its own price. That is the difference between a topping
 * and a parameter: repricing Sucuk is an edit here and nowhere else.
 */
public class Sausage extends Topping {

    public Sausage(Toastable component) {
        super(component, "Sucuk", 3);
    }
}
