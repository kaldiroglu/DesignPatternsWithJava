package dev.kaldiroglu.dp.structural.flyweight.hw.tiles;

/**
 * <b>ConcreteFlyweight</b> — a kind of map tile: its artwork and what it costs to cross.
 *
 * <p>A 2000x2000 map is four million squares. It is not four million kinds of ground.</p>
 */
public final class Tile {

    private final String terrain;
    private final int movementCost;
    private final byte[] sprite;

    Tile(String terrain, int movementCost, byte[] sprite) {
        this.terrain = terrain;
        this.movementCost = movementCost;
        this.sprite = sprite.clone();
    }

    public String terrain() {
        return terrain;
    }

    public int movementCost() {
        return movementCost;
    }

    public int spriteBytes() {
        return sprite.length;
    }

    /** Renders at a position supplied by the caller — the extrinsic state. */
    public String render(int row, int column) {
        return terrain + "@(" + row + "," + column + ")";
    }
}
