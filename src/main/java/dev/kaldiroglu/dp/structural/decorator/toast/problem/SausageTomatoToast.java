package dev.kaldiroglu.dp.structural.decorator.toast.problem;

/**
 * Sausage toast, with tomato.
 * <p>
 * The {@code + 2} for tomato appears here for the second time. There is no class named Tomato
 * anywhere in this package, so the price of tomato is not stored, it is <em>scattered</em>.
 * Repricing tomato means finding every subclass that happens to add 2 and deciding, for each
 * one, whether that 2 meant tomato or meant something else that also costs 2.
 */
public class SausageTomatoToast extends SausageToast {

    public SausageTomatoToast() {
        name = "Sausage tomato toast";
    }

    @Override
    public int calculatePrice() {
        return super.calculatePrice() + 2;
    }
}
