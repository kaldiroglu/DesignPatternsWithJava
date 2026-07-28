package dev.kaldiroglu.dp.structural.flyweight.circles.solution;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <b>FlyweightFactory</b> for {@link CircleStyle}.
 *
 * <p>The problem version's factory had a {@code create()} that always allocated. This one
 * answers the question that makes a factory a flyweight factory: have I made this before?</p>
 */
public class CircleStyleFactory {

    private static final float STROKE_WIDTH = 5.0f;

    private final Map<String, CircleStyle> styles = new LinkedHashMap<>();
    private int requests;

    /** The shared style for this radius and color, created on first request only. */
    public CircleStyle getStyle(int radius, Color color) {
        requests++;
        String key = radius + "|" + color.getRGB();
        return styles.computeIfAbsent(key, k -> new CircleStyle(radius, color, STROKE_WIDTH));
    }

    public int distinctStyles() {
        return styles.size();
    }

    public int requestCount() {
        return requests;
    }

    /** Requests that cost no allocation because the style already existed. */
    public int sharedCount() {
        return requests - styles.size();
    }
}
