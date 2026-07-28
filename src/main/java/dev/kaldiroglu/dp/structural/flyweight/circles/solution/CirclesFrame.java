package dev.kaldiroglu.dp.structural.flyweight.circles.solution;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Container;

/** The window: a canvas, Start, Stop, and a running count of what sharing saved. */
public class CirclesFrame extends JFrame {

    private final transient CircleField field;
    private final CirclesCanvas canvas;
    private final JLabel counts = new JLabel();

    public CirclesFrame(int circles, int delayMillis, int width, int height) {
        // The problem version kept separate width/height fields that the constructor never
        // updated, so circles were scattered inside a rectangle the window did not have.
        setSize(width, height);
        setTitle("Circles — one style object per look, not per circle");

        this.field = new CircleField(width, height, 20260728L);
        field.populate(circles);
        this.canvas = new CirclesCanvas(field, delayMillis);

        Container contentPane = getContentPane();
        contentPane.add(canvas, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton start = new JButton("Start");
        start.addActionListener(e -> {
            canvas.start();
            updateCounts();
        });
        JButton stop = new JButton("Stop");
        stop.addActionListener(e -> canvas.stop());   // and Start works again afterwards
        buttons.add(start);
        buttons.add(stop);
        buttons.add(counts);
        contentPane.add(buttons, BorderLayout.SOUTH);

        updateCounts();
    }

    private void updateCounts() {
        counts.setText(String.format(
                "   %,d circles · %d distinct styles · %,d allocations avoided",
                field.size(), field.distinctStyles(), field.sharedCount()));
    }
}
