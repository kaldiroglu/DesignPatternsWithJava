package dev.kaldiroglu.dp.structural.decorator.toast.solution;

/**
 * A ConcreteDecorator: adds Tomato to whatever it is wrapped around.
 * <p>
 * The class knows its own name and its own price. That is the difference between a topping
 * and a parameter: repricing Tomato is an edit here and nowhere else.
 */
public class Tomato extends Topping {

    public Tomato(Toastable component) {
        super(component, "Tomato", 2);
    }
}
