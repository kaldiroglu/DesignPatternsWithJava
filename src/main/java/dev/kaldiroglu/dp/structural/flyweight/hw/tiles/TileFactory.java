package dev.kaldiroglu.dp.structural.flyweight.hw.tiles;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>FlyweightFactory</b> for map tiles, with the sprite loaded here rather than passed in.
 *
 * <p>Loading inside the factory is the detail worth copying. If the caller supplies the
 * sprite, the factory must either key on it or silently ignore it, and ignoring it is how a
 * winter texture ends up rendered as a summer one with nothing in the log.</p>
 */
public class TileFactory {

    private final Map<String, Tile> tiles = new HashMap<>();
    private final SpriteLoader loader;
    private int requests;

    public TileFactory(SpriteLoader loader) {
        this.loader = loader;
    }

    public Tile get(String terrain, int movementCost) {
        requests++;
        String key = terrain + "|" + movementCost;
        return tiles.computeIfAbsent(key,
                k -> new Tile(terrain, movementCost, loader.load(terrain)));
    }

    public int distinctTiles() {
        return tiles.size();
    }

    public int requestCount() {
        return requests;
    }

    public long spriteBytes() {
        long total = 0;
        for (Tile tile : tiles.values()) {
            total += tile.spriteBytes();
        }
        return total;
    }
}
