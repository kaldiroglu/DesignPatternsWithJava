package dev.kaldiroglu.dp.structural.flyweight.circles.solution;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The circles themselves, with no Swing anywhere.
 *
 * <p>Splitting this out from the canvas is what makes the example testable: the interesting
 * claim — ten thousand circles, a hundred styles — has nothing to do with a window, and a
 * test should not need a display to check it.</p>
 */
public class CircleField {

    private static final Color[] PALETTE = {
            Color.RED, Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY,
            Color.GRAY, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK,
    };
    private static final int RADII = 10;

    private final List<Circle> circles = new ArrayList<>();
    private final CircleStyleFactory factory = new CircleStyleFactory();
    private final Random random;
    private final int width;
    private final int height;

    /** Seeded on purpose: a test that cannot repeat itself cannot assert a number. */
    public CircleField(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.random = new Random(seed);
    }

    public void populate(int count) {
        for (int i = 0; i < count; i++) {
            circles.add(new Circle(randomPoint(), randomStyle()));
        }
    }

    /** Moves every circle. Only positions change — the shared styles are untouched. */
    public void scatter() {
        for (Circle circle : circles) {
            circle.moveTo(randomPoint());
        }
    }

    public void draw(Graphics2D g2) {
        for (Circle circle : circles) {
            circle.draw(g2);
        }
    }

    private Point randomPoint() {
        return new Point(random.nextInt(width), random.nextInt(height));
    }

    private CircleStyle randomStyle() {
        int radius = 10 + 20 * random.nextInt(RADII);
        return factory.getStyle(radius, PALETTE[random.nextInt(PALETTE.length)]);
    }

    public int size() {
        return circles.size();
    }

    public int distinctStyles() {
        return factory.distinctStyles();
    }

    public int sharedCount() {
        return factory.sharedCount();
    }

    /** The ceiling on distinct styles: every radius against every color. */
    public static int possibleStyles() {
        return RADII * PALETTE.length;
    }

    public Circle circle(int index) {
        return circles.get(index);
    }
}
