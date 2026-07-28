package dev.kaldiroglu.dp.structural.flyweight.forest.solution;

/**
 * One tree in the forest: a position, and a pointer to the kind it is.
 *
 * <p>Three fields where the problem version had five, and — the part that matters — the two
 * it dropped were the two that were identical on every tree of the same kind.</p>
 */
public class Tree {

    private final int x;
    private final int y;
    private final TreeType type;   // shared with every other tree of this kind

    public Tree(int x, int y, TreeType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /** Passes this tree's position — the extrinsic state — into the shared flyweight. */
    public void draw() {
        type.draw(x, y);
    }

    public TreeType type() {
        return type;
    }
}
