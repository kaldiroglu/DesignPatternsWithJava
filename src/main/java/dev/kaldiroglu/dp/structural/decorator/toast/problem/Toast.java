package dev.kaldiroglu.dp.structural.decorator.toast.problem;

/**
 * What every toast on the menu can do: say what it is called and what it costs.
 * <p>
 * The interface is not the problem. Everything that goes wrong in this package goes wrong in
 * the class hierarchy underneath it.
 */
public interface Toast {

    int calculatePrice();

    String getName();
}
