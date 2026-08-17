package dev.kaldiroglu.dp.structural.composite.gof.graphics;

/**
 * Leaf role of the Composite solution (GoF, p. 165) — a primitive graphic.
 *
 * <p>Like {@link Line}, a {@code Rectangle} is childless: it draws itself and
 * rejects every child operation it inherits from {@link Graphic}.</p>
 */
public class Rectangle extends Graphic {

    private final int width;
    private final int height;

    public Rectangle(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    @Override
    public void draw(Point at) {
        System.out.println("Rectangle " + width + "x" + height + " drawn at " + at);
    }
}
