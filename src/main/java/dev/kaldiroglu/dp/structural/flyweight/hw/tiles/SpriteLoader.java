package dev.kaldiroglu.dp.structural.flyweight.hw.tiles;

import java.util.ArrayList;
import java.util.List;

/** Loads tile artwork and remembers every load, so the saving can be counted. */
public class SpriteLoader {

    private final int bytesPerSprite;
    private final List<String> loaded = new ArrayList<>();

    public SpriteLoader(int bytesPerSprite) {
        this.bytesPerSprite = bytesPerSprite;
    }

    public byte[] load(String terrain) {
        loaded.add(terrain);
        return new byte[bytesPerSprite];
    }

    public int loadCount() {
        return loaded.size();
    }

    public List<String> loadedTerrains() {
        return List.copyOf(loaded);
    }
}
