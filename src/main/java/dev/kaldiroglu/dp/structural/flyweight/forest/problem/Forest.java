package dev.kaldiroglu.dp.structural.flyweight.forest.problem;

import java.util.ArrayList;
import java.util.List;

/** A forest of self-contained trees. Every call to {@link #plant} allocates a whole one. */
public class Forest {

    private final List<Tree> trees = new ArrayList<>();

    public void plant(int x, int y, String name, String color, byte[] texture) {
        trees.add(new Tree(x, y, name, color, texture));
    }

    public void render() {
        for (Tree t : trees) {
            t.draw();
        }
    }

    public int size() {
        return trees.size();
    }

    /** Total texture bytes held by the forest — the number the solution has to beat. */
    public long textureBytes() {
        long total = 0;
        for (Tree t : trees) {
            total += t.textureBytes();
        }
        return total;
    }
}
