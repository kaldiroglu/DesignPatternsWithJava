package dev.kaldiroglu.dp.behavioral.memento.game.problem.copy;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage three: the player can make a copy of itself, and take its values back from a copy.
 * <p>
 * The game keeps the copies, so the player has no save slots, and nothing outside sees the
 * fields. But the copy constructor copies the reference to the inventory list, not the list:
 * the copy and the player share one list. Whatever the player picks up or drops after the
 * checkpoint, the checkpoint picks up or drops too.
 */
public final class Player {

    private int health = 100;
    private String position = "start";
    private List<String> inventory = new ArrayList<>();

    public Player() {
    }

    /** A copy of another player — the list is shared, not copied. */
    public Player(Player other) {
        this.health = other.health;
        this.position = other.position;
        this.inventory = other.inventory;
    }

    public void takeDamage(int amount) {
        health = Math.max(0, health - amount);
    }

    public void moveTo(String place) {
        position = place;
    }

    public void pickUp(String item) {
        inventory.add(item);
    }

    public void drop(String item) {
        inventory.remove(item);
    }

    public void restoreFrom(Player copy) {
        this.health = copy.health;
        this.position = copy.position;
        this.inventory = new ArrayList<>(copy.inventory);
    }

    @Override
    public String toString() {
        return "health " + health + ", at " + position + ", carrying " + inventory;
    }
}
