package dev.kaldiroglu.dp.structural.flyweight.forest.solution;

import java.util.ArrayList;
import java.util.List;

/** A forest whose trees know where they are and nothing else. */
public class Forest {

    private final List<Tree> trees = new ArrayList<>();
    private final TreeFactory factory;

    public Forest(TreeFactory factory) {
        this.factory = factory;
    }

    public void plant(int x, int y, String name, String color) {
        trees.add(new Tree(x, y, factory.getType(name, color)));
    }

    public void render() {
        for (Tree t : trees) {
            t.draw();
        }
    }

    public int size() {
        return trees.size();
    }

    public int distinctTypes() {
        return factory.distinctTypes();
    }

    public long textureBytes() {
        return factory.textureBytes();
    }

    Tree tree(int index) {
        return trees.get(index);
    }
}
