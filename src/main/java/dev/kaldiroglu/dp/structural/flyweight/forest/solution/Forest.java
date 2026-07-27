package dev.kaldiroglu.dp.structural.flyweight.forest.solution;

import java.util.ArrayList;
import java.util.List;

public class Forest {

    private final List<Tree> trees = new ArrayList<>();
    private final TreeFactory factory = new TreeFactory();

    public void plant(int x, int y, String name, String color, byte[] texture) {
        TreeType type = factory.getType(name, color, texture);
        trees.add(new Tree(x, y, type));
    }

    public void render() {
        for (Tree t : trees) t.draw();
    }

    public int size() { return trees.size(); }
    public int distinctTypes() { return factory.distinctTypes(); }
}
