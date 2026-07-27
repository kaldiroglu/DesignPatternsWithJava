package dev.kaldiroglu.dp.structural.adapter.gof;

/**
 * <b>Object Adapter</b> (GoF p. 147).
 *
 * <p>Adapts {@link TextView} to the {@link Shape} interface by <i>holding a reference</i> to a
 * {@code TextView} and forwarding/translating each call. This is the form GoF recommends for
 * single-inheritance languages: it uses <b>composition</b> rather than inheritance, so a single
 * adapter class can wrap <i>any</i> {@code TextView} instance (or subclass).</p>
 *
 * <p>Contrast with {@link dev.kaldiroglu.adapter.gof.classadapter.TextShape the class adapter},
 * which inherits {@code TextView} instead.</p>
 */
public class TextShape implements Shape {

    /** The wrapped adaptee. */
    private final TextView textView;

    public TextShape(TextView textView) {
        this.textView = textView;
    }

    /**
     * Translates the adaptee's {@code getOrigin}/{@code getExtent} into the {@code boundingBox}
     * the {@link Shape} interface promises. This method <i>is</i> the adaptation.
     */
    @Override
    public BoundingBox boundingBox() {
        Point origin = textView.getOrigin();
        Point extent = textView.getExtent();
        Point bottomLeft = origin;
        Point topRight = new Point(origin.x() + extent.x(), origin.y() + extent.y());
        return new BoundingBox(bottomLeft, topRight);
    }

    @Override
    public Manipulator createManipulator() {
        return new TextManipulator(this);
    }

    /** Delegates straight to the adaptee &ndash; the names happen to match. */
    @Override
    public boolean isEmpty() {
        return textView.isEmpty();
    }

    @Override
    public String toString() {
        return "TextShape wrapping " + textView;
    }
}
