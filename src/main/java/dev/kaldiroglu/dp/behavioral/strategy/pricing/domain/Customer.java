package dev.kaldiroglu.dp.behavioral.strategy.pricing.domain;

/**
 * Who is at the till, and the two facts a campaign is allowed to ask about them.
 *
 * @param name    for the receipt
 * @param student whether they showed a student card
 * @param staff   whether they work here
 */
public record Customer(String name, boolean student, boolean staff) {

    public static Customer shopper(String name) {
        return new Customer(name, false, false);
    }

    public static Customer student(String name) {
        return new Customer(name, true, false);
    }

    public static Customer staff(String name) {
        return new Customer(name, false, true);
    }
}
