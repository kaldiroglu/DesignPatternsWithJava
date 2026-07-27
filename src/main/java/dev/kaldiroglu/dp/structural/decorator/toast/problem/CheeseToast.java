package dev.kaldiroglu.dp.structural.decorator.toast.problem;

public class CheeseToast extends AbstractToast {

    public CheeseToast() {
        name = "Cheese toast";
    }

    @Override
    public int calculatePrice() {
        // 5 = bread plus cheese, welded into one number. Neither part exists on its own
        // anywhere in this package, so neither can be repriced on its own.
        return 5;
    }
}
