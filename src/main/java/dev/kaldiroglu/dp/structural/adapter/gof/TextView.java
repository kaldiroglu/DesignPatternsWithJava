package dev.kaldiroglu.dp.structural.adapter.gof;

/**
 * <b>Adaptee</b> participant (GoF p. 146).
 *
 * <p>An existing, useful class for displaying and editing text. It is perfectly good code
 * &ndash; we do not want to change it &ndash; but its interface is <i>incompatible</i> with
 * {@link Shape}: it speaks {@code getOrigin}/{@code getExtent}, not
 * {@code boundingBox}/{@code createManipulator}.</p>
 *
 * <p>The whole point of the pattern is to reuse this class <i>without modifying it</i>.</p>
 */
public class TextView {

    private final Point origin;
    private final double width;
    private final double height;
    private final String text;

    public TextView(Point origin, double width, double height, String text) {
        this.origin = origin;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    /** @return the bottom-left corner of the text block. */
    public Point getOrigin() {
        return origin;
    }

    /** @return the size of the text block as a {@code (width, height)} pair. */
    public Point getExtent() {
        return new Point(width, height);
    }

    public boolean isEmpty() {
        return text == null || text.isEmpty();
    }

    @Override
    public String toString() {
        return "TextView[\"" + text + "\"]";
    }
}
