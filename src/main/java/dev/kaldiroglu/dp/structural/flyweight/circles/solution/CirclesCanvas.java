package dev.kaldiroglu.dp.structural.flyweight.circles.solution;

import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * The drawing surface.
 *
 * <p>Animation runs on a Swing {@link Timer}, which fires on the event dispatch thread — so
 * positions are changed and painted by the same thread, and no lock is needed. The problem
 * version animated from its own threads while the EDT painted, and had painting itself
 * request the next repaint, which never stopped.</p>
 */
public class CirclesCanvas extends JPanel {

    private final transient CircleField field;
    private final Timer timer;

    public CirclesCanvas(CircleField field, int delayMillis) {
        this.field = field;
        setBorder(new LineBorder(Color.BLACK, 2));
        setBackground(new Color(255, 255, 210));
        this.timer = new Timer(delayMillis, e -> {
            field.scatter();
            repaint();          // asked for once per tick, not once per paint
        });
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public boolean isRunning() {
        return timer.isRunning();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        field.draw((Graphics2D) g);
    }
}
