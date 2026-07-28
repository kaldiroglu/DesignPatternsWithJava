package dev.kaldiroglu.dp.structural.flyweight.hw.tiles;

/**
 * The map: one shared {@link Tile} reference per square, and nothing else.
 *
 * <p>The array holds references, so the map costs one pointer per square whatever the
 * artwork weighs. That is the whole trade: four million pointers instead of four million
 * sprites.</p>
 */
public class GameMap {

    private final Tile[][] squares;
    private final TileFactory factory;

    public GameMap(int rows, int columns, TileFactory factory) {
        this.squares = new Tile[rows][columns];
        this.factory = factory;
    }

    public void set(int row, int column, String terrain, int movementCost) {
        squares[row][column] = factory.get(terrain, movementCost);
    }

    public Tile at(int row, int column) {
        return squares[row][column];
    }

    /** Total cost of walking a row — read from shared tiles, stored nowhere. */
    public int rowMovementCost(int row) {
        int total = 0;
        for (Tile tile : squares[row]) {
            total += tile.movementCost();
        }
        return total;
    }

    public int squareCount() {
        return squares.length * squares[0].length;
    }

    public int distinctTiles() {
        return factory.distinctTiles();
    }
}
