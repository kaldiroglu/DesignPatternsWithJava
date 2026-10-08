package dev.kaldiroglu.dp.behavioral.state.hw.vending;

import java.util.ArrayList;
import java.util.List;

/**
 * Homework 3: a vending machine with a coin slot and a stock of drinks.
 * <p>
 * Three states: waiting for a coin, has a coin, sold out. The homework question was what
 * happens to a coin put into a sold-out machine. Here it is returned at once: the sold-out
 * state never takes it.
 */
public final class VendingMachine {

    private VendingState state;
    private int stock;
    private final List<String> log = new ArrayList<>();

    public VendingMachine(int stock) {
        this.stock = stock;
        this.state = stock > 0 ? new WaitingForCoin() : new SoldOut();
    }

    public void insertCoin() {
        state = state.insertCoin(this);
    }

    public void pressButton() {
        state = state.pressButton(this);
    }

    public void refill(int drinks) {
        stock += drinks;
        state = state.refilled(this);
    }

    public String state() {
        return state.getClass().getSimpleName();
    }

    int stock() {
        return stock;
    }

    void takeOneDrink() {
        stock--;
    }

    void log(String line) {
        log.add(line);
    }

    public List<String> log() {
        return List.copyOf(log);
    }
}
