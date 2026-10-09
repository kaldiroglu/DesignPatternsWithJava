package dev.kaldiroglu.dp.behavioral.memento.game.problem.history;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Stage two: the player keeps its own checkpoints.
 * <p>
 * Nothing outside sees the fields, and loading is correct. But the player class now also
 * manages save slots: how many to keep, which one to load, when to throw old ones away.
 * Those are decisions of the game, not of the player, and every other object the game wants
 * to save — enemies, doors, chests — needs the same code.
 */
public final class Player {

    private record Saved(int health, String position, List<String> inventory) {
    }

    private int health = 100;
    private String position = "start";
    private final List<String> inventory = new ArrayList<>();
    private final Deque<Saved> checkpoints = new ArrayDeque<>();

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

    public void saveCheckpoint() {
        checkpoints.push(new Saved(health, position, List.copyOf(inventory)));
    }

    public void loadCheckpoint() {
        Saved saved = checkpoints.peek();
        health = saved.health();
        position = saved.position();
        inventory.clear();
        inventory.addAll(saved.inventory());
    }

    @Override
    public String toString() {
        return "health " + health + ", at " + position + ", carrying " + inventory;
    }
}
