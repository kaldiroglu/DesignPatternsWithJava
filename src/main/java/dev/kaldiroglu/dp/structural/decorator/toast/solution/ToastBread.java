package dev.kaldiroglu.dp.structural.decorator.toast.solution;

import java.util.List;

/**
 * The ConcreteComponent role: the thing being decorated, which is a slice of toast bread.
 * <p>
 * It knows nothing about toppings. That ignorance is the point, and is why adding a topping
 * never means editing this class.
 */
public class ToastBread implements Toastable {

    private final String name;
    private final int price;

    public ToastBread() {
        this("Toast bread", 5);
    }

    public ToastBread(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    @Override
    public int calculatePrice() {
        return price;
    }

    @Override
    public List<Topping> getToppings() {
        return List.of();
    }

    @Override
    public String toString() {
        return name + " (" + price + ")";
    }
}
