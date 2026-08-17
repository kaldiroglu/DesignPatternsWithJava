package dev.kaldiroglu.dp.structural.decorator.toast.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The Decorator role: a topping is itself something you can order, and it holds the thing it
 * was added to.
 * <p>
 * Both halves of that sentence matter, and they are the solution in one class. {@code Topping}
 * <em>implements</em> {@link Toastable}, so a topped toast can go anywhere a plain one can.
 * It also <em>holds</em> a {@code Toastable}, so it can pass the question down and add its own
 * answer to whatever comes back. Being both is what lets toppings stack without limit.
 */
public abstract class Topping implements Toastable {

    /** The toast this topping was added to: bare bread, or another topping. */
    protected final Toastable component;

    private final String name;
    private final int price;

    protected Topping(Toastable component, String name, int price) {
        this.component = Objects.requireNonNull(component, "a topping must be added to something");
        this.name = name;
        this.price = price;
    }

    @Override
    public int calculatePrice() {
        // Forward, then add. The topping never asks what it is sitting on.
        return component.calculatePrice() + price;
    }

    @Override
    public List<Topping> getToppings() {
        // Copy before adding. The list handed back belongs to whoever asked for it, and a
        // decorator that appended to a list it did not own would corrupt the toast below.
        List<Topping> toppings = new ArrayList<>(component.getToppings());
        toppings.add(this);
        return toppings;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    /** The toast underneath this topping. */
    public Toastable getComponent() {
        return component;
    }

    @Override
    public String toString() {
        return "Topping [name=" + name + ", price=" + price + "]";
    }
}
