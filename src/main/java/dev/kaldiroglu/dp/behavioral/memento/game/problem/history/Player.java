package dev.kaldiroglu.dp.behavioral.memento.game.problem.history;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Stage two: the player keeps its own checkpoints.
 * <p>
 * Nothing outside sees the fields, and loading is correct. But the saves are now a field of
 * the player. The game can only say "save" or "load", and loading always gives back the
 * last save. Three save slots and an autosave would all be new code in this class.
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
