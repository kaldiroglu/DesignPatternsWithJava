package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.methods;

/** Stage one: the item computes its own tax, shipping cost and receipt line. */
public record Electronics(String name, int price) implements Item {

    @Override
    public int tax() {
        return price * 20 / 100;
    }

    @Override
    public int shippingCost() {
        return 25;
    }

    @Override
    public String receiptLine() {
        return name + " " + price + " (tax " + tax() + ")";
    }
}
