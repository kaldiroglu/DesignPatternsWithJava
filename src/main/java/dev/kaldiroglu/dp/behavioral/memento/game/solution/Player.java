package dev.kaldiroglu.dp.behavioral.memento.game.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * The <b>Originator</b>: the player makes its own checkpoint, and loads one.
 * <p>
 * {@link #save()} returns a {@link Checkpoint} that holds a copy of the state: the health,
 * the position and a copy of the inventory list. Nothing the player does afterwards can
 * change it. Only the player can read it back, in {@link #load}.
 */
public final class Player {

    /**
     * The <b>Memento</b>. Its fields are private, and it has no getters: the game can keep it
     * and hand it back, but cannot look inside or change it. Because it is nested in
     * {@code Player}, the player can read its private fields.
     */
    public static final class Checkpoint {
        private final int health;
        private final String position;
        private final List<String> inventory;

        private Checkpoint(int health, String position, List<String> inventory) {
            this.health = health;
            this.position = position;
            this.inventory = List.copyOf(inventory);
        }
    }

    private int health = 100;
    private String position = "start";
    private final List<String> inventory = new ArrayList<>();

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

    public Checkpoint save() {
        return new Checkpoint(health, position, inventory);
    }

    public void load(Checkpoint checkpoint) {
        health = checkpoint.health;
        position = checkpoint.position;
        inventory.clear();
        inventory.addAll(checkpoint.inventory);
    }

    @Override
    public String toString() {
        return "health " + health + ", at " + position + ", carrying " + inventory;
    }
}
