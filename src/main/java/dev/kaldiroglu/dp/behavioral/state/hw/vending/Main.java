package dev.kaldiroglu.dp.behavioral.state.hw.vending;

/**
 * A vending machine with one drink: a drink is sold, the machine is sold out and returns
 * the next coin, and a refill makes it wait for a coin again.
 */
public final class Main {

    public static void main(String[] args) {
        VendingMachine machine = new VendingMachine(1);
        machine.pressButton();
        machine.insertCoin();
        machine.insertCoin();
        machine.pressButton();
        System.out.println("After one sale: " + machine.state());
        machine.insertCoin();
        machine.refill(5);
        System.out.println("After a refill: " + machine.state());
        System.out.println("Log: " + machine.log());
    }
}
