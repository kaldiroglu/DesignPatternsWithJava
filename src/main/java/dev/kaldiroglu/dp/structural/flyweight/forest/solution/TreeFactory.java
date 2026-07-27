package dev.kaldiroglu.dp.structural.flyweight.forest.solution;

import java.util.HashMap;
import java.util.Map;

public class TreeFactory {

    private final Map<String, TreeType> types = new HashMap<>();

    public TreeType getType(String name, String color, byte[] texture) {
        String key = name + "|" + color;
        TreeType existing = types.get(key);
        if (existing != null) return existing;

        TreeType created = new TreeType(name, color, texture);
        types.put(key, created);
        return created;
    }

    public int distinctTypes() { return types.size(); }
}
