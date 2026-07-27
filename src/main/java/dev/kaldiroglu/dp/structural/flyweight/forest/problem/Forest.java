package dev.kaldiroglu.dp.structural.flyweight.forest.problem;

import java.util.ArrayList;
import java.util.List;

public class Forest {

    private final List<Tree> trees = new ArrayList<>();

    public void plant(int x, int y, String name, String color, byte[] texture) {
        // each call allocates a fresh Tree carrying its own texture copy
        trees.add(new Tree(x, y, name, color, texture));
    }

    public void render() {
        for (Tree t : trees) t.draw();
    }

    public int size() { return trees.size(); }
}
