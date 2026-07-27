package dev.kaldiroglu.dp.structural.decorator.toast.solution;

/**
 * A ConcreteDecorator: adds Russian salad to whatever it is wrapped around.
 * <p>
 * The class knows its own name and its own price. That is the difference between a topping
 * and a parameter: repricing Russian salad is an edit here and nowhere else.
 */
public class Salad extends Topping {

    public Salad(Toastable component) {
        super(component, "Russian salad", 2);
    }
}
