package dev.kaldiroglu.dp.structural.flyweight.circles.solution;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Runs the animation, and prints the same numbers the window shows for anyone reading a log.
 *
 * <p>Ten thousand circles. At most a hundred ways for a circle to look — ten radii against
 * ten colors — so at most a hundred style objects, however long it runs.</p>
 */
public class Main {

    public static void main(String[] args) {
        int circles = 10_000;

        CircleField field = new CircleField(800, 1000, 20260728L);
        field.populate(circles);

        System.out.println("Circles on the canvas       : " + field.size());
        System.out.println("Possible ways to look       : " + CircleField.possibleStyles());
        System.out.println("Style objects allocated     : " + field.distinctStyles());
        System.out.println("Allocations avoided         : " + field.sharedCount());
        System.out.println();
        System.out.println("Moving every circle allocates nothing at all: only positions change,");
        System.out.println("and no position is stored inside a shared style.");

        if (java.awt.GraphicsEnvironment.isHeadless()) {
            System.out.println();
            System.out.println("(headless: the window is skipped)");
            return;
        }
        SwingUtilities.invokeLater(() -> {
            CirclesFrame frame = new CirclesFrame(circles, 40, 800, 1000);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
