package dev.kaldiroglu.dp.behavioral.visitor.checkout.problem.methods;

/**
 * Stage one: every operation is a method on the item.
 * <p>
 * Each item class knows its own tax, its own shipping cost and its own receipt line. It
 * works, and it is fast to write. But the tax rules are now in three classes, and the next
 * operation — customs codes, loyalty points — is an edit to every one of them. The item
 * classes belong to the catalog; the operations belong to checkout.
 */
public interface Item {

    String name();

    int price();

    int tax();

    int shippingCost();

    String receiptLine();
}
