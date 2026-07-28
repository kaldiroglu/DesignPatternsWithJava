package dev.kaldiroglu.dp.structural.flyweight.circles.solution;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * <b>ConcreteFlyweight</b> — how a circle looks: its radius, its color, its stroke.
 *
 * <p>Immutable, and created only by {@link CircleStyleFactory}. A canvas showing ten thousand
 * circles in ten colors and ten sizes needs a hundred of these, not ten thousand.</p>
 *
 * <p>The stroke is the clearest case of intrinsic state in the example. The problem version
 * held one {@code static} stroke shared by every circle — which was the right instinct, in
 * the wrong place: state shared through a static is shared by everything whether that is
 * correct or not, while a flyweight is shared by exactly the objects that agree on it.</p>
 */
public final class CircleStyle {

    private final int radius;
    private final Color color;
    private final Stroke stroke;

    CircleStyle(int radius, Color color, float strokeWidth) {
        this.radius = radius;
        this.color = color;
        this.stroke = new BasicStroke(strokeWidth);
    }

    /**
     * Draws a circle of this style at a position supplied by the caller.
     *
     * <p>GoF, p. 198: the flyweight interface "enables flyweights to receive and act on
     * extrinsic state". The position is the extrinsic state, and it arrives as an argument
     * rather than a field — which is precisely what lets one style serve every circle that
     * looks like this.</p>
     */
    public void draw(Graphics2D g2, Point center) {
        g2.setColor(color);
        g2.setStroke(stroke);
        g2.drawOval(center.x() - radius, center.y() - radius, radius * 2, radius * 2);
    }

    public int radius() {
        return radius;
    }

    public Color color() {
        return color;
    }
}
