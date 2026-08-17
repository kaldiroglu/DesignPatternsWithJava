package dev.kaldiroglu.dp.structural.composite.gof.graphics;

/**
 * Client of the Composite solution — the "graphics editor" of GoF p. 163.
 *
 * <p>The client builds a tree and then issues a single request to its root. It
 * never distinguishes a primitive from a container.</p>
 */
public final class Demo {

    private Demo() {
    }

    public static void main(String[] args) {
        // A small drawing: two primitives plus a nested picture.
        Picture drawing = new Picture("drawing");
        drawing.add(new Line(100));
        drawing.add(new Text("Composite"));

        Picture logo = new Picture("logo");
        logo.add(new Rectangle(40, 20));
        logo.add(new Line(40));
        drawing.add(logo); // a Picture inside a Picture — arbitrary depth

        System.out.println("--- Drawing the whole tree with one call ---");
        drawing.draw(new Point(0, 0));

        System.out.println();
        System.out.println("--- The client treats a leaf exactly the same way ---");
        Graphic anything = new Text("a lone leaf");
        anything.draw(new Point(5, 5)); // same call, no type test

        System.out.println();
        System.out.println("--- Transparency has a price: leaves reject child operations ---");
        try {
            anything.add(new Line(1));
        } catch (UnsupportedOperationException e) {
            System.out.println("Rejected as expected: " + e.getMessage());
        }
    }
}
