package dev.kaldiroglu.dp.behavioral.memento.game.problem.setters;

import java.util.List;

/** Stage one: the game copies the fields out, and writes them back. */
public final class Game {

    private int savedHealth;
    private String savedPosition;
    private List<String> savedInventory;

    public void checkpoint(Player player) {
        savedHealth = player.getHealth();
        savedPosition = player.getPosition();
        savedInventory = player.getInventory();
    }

    public void load(Player player) {
        player.setHealth(savedHealth);
        player.setPosition(savedPosition);
        player.setInventory(savedInventory);
    }
}
