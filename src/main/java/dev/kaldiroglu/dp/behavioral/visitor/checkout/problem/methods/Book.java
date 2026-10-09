package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.methods;

/** Stage one: the item computes its own tax, shipping cost and receipt line. */
public record Book(String name, int price) implements Item {

    @Override
    public int tax() {
        return price * 5 / 100;
    }

    @Override
    public int shippingCost() {
        return 10;
    }

    @Override
    public String receiptLine() {
        return name + " " + price + " (tax " + tax() + ")";
    }
}
