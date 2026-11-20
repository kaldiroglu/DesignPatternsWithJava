package dev.kaldiroglu.dp.behavioral.memento.game.problem.copyfix;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage three with the quick fix: the list is copied when the game saves and again when it
 * loads.
 * <p>
 * The two lines that differ from {@code problem.copy.Player} are the inventory lines in the
 * copy constructor and in {@code restoreFrom}. Each one makes a new list.
 * <ul>
 *   <li>Without the copy in the constructor, the save and the player share one list, so
 *       what the player drops after the save is gone from the save too.</li>
 *   <li>Without the copy in {@code restoreFrom}, the player takes the save's own list when
 *       it loads, so what it picks up after the load goes into the save too.</li>
 * </ul>
 * The checkpoint is still a whole {@code Player}, so the game can still read and change it.
 * Copying the list does not fix that.
 */
public final class Player {

    private int health = 100;
    private String position = "start";
    private List<String> inventory = new ArrayList<>();

    public Player() {
    }

    /** A copy of another player, with its own copy of the list. */
    public Player(Player other) {
        this.health = other.health;
        this.position = other.position;
        this.inventory = new ArrayList<>(other.inventory);
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

    /** Takes the values back from a copy, field by field, and copies the list again. */
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
