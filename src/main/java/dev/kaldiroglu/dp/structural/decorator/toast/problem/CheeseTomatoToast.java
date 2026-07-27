package dev.kaldiroglu.dp.structural.decorator.toast.problem;

/**
 * Cheese toast, with tomato.
 * <p>
 * Read the {@code extends} clause as the claim it actually makes: a cheese-and-tomato toast
 * <em>is a</em> cheese toast. Inheritance is being used here to reuse the {@code + 2} rather
 * than to express a genuine subtype. It also freezes the order, because "tomato added to a
 * cheese toast" has become a type rather than a decision the customer makes at the counter.
 */
public class CheeseTomatoToast extends CheeseToast {

    public CheeseTomatoToast() {
        name = "Cheese tomato toast";
    }

    @Override
    public int calculatePrice() {
        return super.calculatePrice() + 2;
    }
}
