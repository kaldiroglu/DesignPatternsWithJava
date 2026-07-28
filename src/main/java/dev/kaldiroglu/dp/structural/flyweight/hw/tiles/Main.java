package dev.kaldiroglu.dp.structural.flyweight.hw.tiles;

/** Builds a 500x500 map out of five kinds of ground and reports what that cost. */
public class Main {

    private static final String[] TERRAIN = {"Grass", "Water", "Rock", "Sand", "Forest"};
    private static final int[] COST = {1, 5, 3, 2, 4};
    private static final int SIZE = 500;
    private static final int SPRITE_BYTES = 64 * 1024;

    public static void main(String[] args) {
        SpriteLoader loader = new SpriteLoader(SPRITE_BYTES);
        TileFactory factory = new TileFactory(loader);
        GameMap map = new GameMap(SIZE, SIZE, factory);

        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                int kind = (row * 7 + column * 13) % TERRAIN.length;
                map.set(row, column, TERRAIN[kind], COST[kind]);
            }
        }

        System.out.printf("Squares on the map     : %,d%n", map.squareCount());
        System.out.println("Kinds of ground        : " + TERRAIN.length);
        System.out.println("Tile objects allocated : " + map.distinctTiles());
        System.out.println("Sprites loaded         : " + loader.loadCount());
        System.out.printf("Sprite bytes held      : %,d%n", factory.spriteBytes());
        System.out.printf("Bytes if each square owned its sprite: %,d%n",
                (long) map.squareCount() * SPRITE_BYTES);
        System.out.println();
        System.out.println("Walking row 0 costs    : " + map.rowMovementCost(0));
    }
}
