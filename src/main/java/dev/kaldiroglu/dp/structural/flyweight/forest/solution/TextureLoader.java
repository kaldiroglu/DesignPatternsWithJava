package dev.kaldiroglu.dp.structural.flyweight.forest.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads a texture for a kind of tree, and records every load so a test can count them.
 *
 * <p>An instance rather than a static, unlike the problem's version — once loading is the
 * factory's job it is worth being able to hand a different loader to a test.</p>
 */
public class TextureLoader {

    private final int size;
    private final List<String> loaded = new ArrayList<>();

    public TextureLoader(int size) {
        this.size = size;
    }

    public byte[] load(String kind) {
        loaded.add(kind);
        return new byte[size];
    }

    public int loadCount() {
        return loaded.size();
    }

    public List<String> loadedKinds() {
        return List.copyOf(loaded);
    }
}
