package dev.kaldiroglu.dp.behavioral.memento.game.problem.setters;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage one: the game saves the player by reading every field, and loads it by writing
 * every field back.
 * <p>
 * So every field needs a public getter and a public setter. Now any code can call
 * {@code setHealth(999)} — the rule "health only changes through damage and healing" is
 * gone. And the game's save code lists the fields one by one, so a field added later is not
 * saved until someone remembers to add it there too.
 */
public final class Player {

    private int health = 100;
    private String position = "start";
    private List<String> inventory = new ArrayList<>();

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

    // Needed only so that the game can save and load:
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public List<String> getInventory() { return List.copyOf(inventory); }
    public void setInventory(List<String> inventory) { this.inventory = new ArrayList<>(inventory); }

    @Override
    public String toString() {
        return "health " + health + ", at " + position + ", carrying " + inventory;
    }
}
