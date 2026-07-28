package dev.kaldiroglu.dp.structural.flyweight.forest.solution;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>FlyweightFactory</b>. Hands back the existing {@link TreeType} for a kind of tree, or
 * creates it the first time it is asked.
 *
 * <p>The key names the intrinsic state completely — name <em>and</em> color. An earlier
 * version keyed on the same two but then accepted a {@code texture} argument it silently
 * ignored whenever the type already existed: ask for a green oak with a winter texture after
 * a green oak with a summer one and you got the summer texture, with nothing to tell you.
 * A flyweight factory has to be honest about what its key covers, so the texture is loaded
 * <em>here</em>, from the kind, and callers no longer supply one.</p>
 */
public class TreeFactory {

    private final Map<String, TreeType> types = new HashMap<>();
    private final TextureLoader loader;
    private int requests;

    public TreeFactory(TextureLoader loader) {
        this.loader = loader;
    }

    /** Returns the shared type for this kind of tree, loading its texture at most once. */
    public TreeType getType(String name, String color) {
        requests++;
        String key = name + "|" + color;
        return types.computeIfAbsent(key, k -> new TreeType(name, color, loader.load(name)));
    }

    public int distinctTypes() {
        return types.size();
    }

    public int requestCount() {
        return requests;
    }

    /** Total texture bytes the whole forest holds, however many trees there are. */
    public long textureBytes() {
        long total = 0;
        for (TreeType type : types.values()) {
            total += type.textureBytes();
        }
        return total;
    }
}
