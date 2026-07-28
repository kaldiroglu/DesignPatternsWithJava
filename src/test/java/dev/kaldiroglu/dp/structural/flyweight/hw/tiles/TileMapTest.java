package dev.kaldiroglu.dp.structural.flyweight.hw.tiles;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Homework 1 — the tile map.
 * <p>
 * The lesson is the ratio: a map has millions of squares and a handful of kinds of ground.
 */
class TileMapTest {

    private static final int SPRITE = 64 * 1024;
    private static final String[] TERRAIN = {"Grass", "Water", "Rock", "Sand", "Forest"};
    private static final int[] COST = {1, 5, 3, 2, 4};

    private static GameMap map(int size, SpriteLoader loader) {
        TileFactory factory = new TileFactory(loader);
        GameMap map = new GameMap(size, size, factory);
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                int kind = (row * 7 + column * 13) % TERRAIN.length;
                map.set(row, column, TERRAIN[kind], COST[kind]);
            }
        }
        return map;
    }

    @Test
    @DisplayName("a quarter of a million squares cost five objects")
    void theMapCostsOneObjectPerKind() {
        SpriteLoader loader = new SpriteLoader(SPRITE);
        GameMap map = map(500, loader);

        assertEquals(250_000, map.squareCount());
        assertEquals(5, map.distinctTiles());
        assertEquals(5, loader.loadCount(), "five sprites loaded, not 250,000");
    }

    @Test
    @DisplayName("the sprite memory is bounded by the kinds, not by the map size")
    void spriteMemoryDoesNotGrowWithTheMap() {
        SpriteLoader small = new SpriteLoader(SPRITE);
        TileFactory smallFactory = new TileFactory(small);
        GameMap tiny = new GameMap(10, 10, smallFactory);

        SpriteLoader large = new SpriteLoader(SPRITE);
        TileFactory largeFactory = new TileFactory(large);
        GameMap big = new GameMap(500, 500, largeFactory);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                tiny.set(i, j, TERRAIN[(i + j) % 5], COST[(i + j) % 5]);
            }
        }
        for (int i = 0; i < 500; i++) {
            for (int j = 0; j < 500; j++) {
                big.set(i, j, TERRAIN[(i + j) % 5], COST[(i + j) % 5]);
            }
        }

        assertEquals(smallFactory.spriteBytes(), largeFactory.spriteBytes(),
                "a map 2,500 times larger holds exactly the same sprite bytes");
        assertEquals(5L * SPRITE, largeFactory.spriteBytes());
    }

    @Test
    @DisplayName("every square of a kind points at the same tile")
    void squaresOfAKindShareOneTile() {
        GameMap map = map(20, new SpriteLoader(SPRITE));

        Tile first = map.at(0, 0);
        Tile same = map.at(5, 5);   // (0*7+0*13)%5 == 0 and (5*7+5*13)%5 == 0

        assertSame(first, same);
        assertEquals("Grass", first.terrain());
    }

    @Test
    @DisplayName("movement cost is read from the shared tile, never copied to the square")
    void extrinsicStateIsReadNotStored() {
        GameMap map = map(10, new SpriteLoader(SPRITE));

        int expected = 0;
        for (int column = 0; column < 10; column++) {
            expected += COST[(column * 13) % 5];
        }
        assertEquals(expected, map.rowMovementCost(0));
    }

    @Test
    @DisplayName("the factory loads the sprite itself, so no caller can supply a wrong one")
    void theFactoryOwnsTheSprite() {
        boolean takesASprite = Arrays.stream(TileFactory.class.getMethods())
                .filter(m -> m.getName().equals("get"))
                .anyMatch(m -> Arrays.stream(m.getParameterTypes())
                        .anyMatch(t -> t == byte[].class));

        assertFalse(takesASprite, "an ignored argument is how the wrong artwork gets rendered");
    }
}
