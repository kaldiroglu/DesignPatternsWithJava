package dev.kaldiroglu.dp.structural.composite.graphic;

/**
 * A display with shapes on it, and a group nested inside.
 * <p>
 * Watch what the client has to know. Building the tree needs a {@link CompositeGraphic};
 * drawing it needs only a {@link Graphic}. That split is the cost of keeping
 * {@code addGraphic} off the Component, and the compile error at the bottom is what it buys.
 */
public class Main {

    public static void main(String[] args) {
        Canvas display = new Canvas("Display", "Light Green");
        display.addGraphic(new Circle("Red Circle", "Red"));
        display.addGraphic(new Circle("Blue Circle", "Blue"));
        display.addGraphic(new Ellipse("Black Ellipse", "Black"));

        // A canvas inside a canvas — the whole point of the pattern.
        Canvas logo = new Canvas("Logo", "White");
        logo.addGraphic(new Triangle("Triangle", "Yellow"));
        logo.addGraphic(new Rectangle("Rectangle", "Green"));
        display.addGraphic(logo);

        display.listGraphic();
        System.out.println("****************");
        display.draw();
        System.out.println("****************");

        System.out.println();
        System.out.println("shapes on the display: " + display.shapeCount());
        System.out.println("shapes in the logo   : " + logo.shapeCount());
        System.out.println("a single circle      : " + new Circle("c", "Red").shapeCount());
        System.out.println("  The client asked one object. Five shapes answered, two of them");
        System.out.println("  a level down, and nobody wrote a loop.");

        System.out.println();
        Graphic asComponent = display;   // a Canvas is a Graphic, so this needs no cast
        asComponent.draw();

        System.out.println();
        System.out.println("A leaf cannot be given children, and the compiler says so:");
        System.out.println("    new Circle(\"c\", \"Red\").addGraphic(..)   does not compile");
        System.out.println("That is the safety this package chose. Its price is that a client");
        System.out.println("building a tree must hold CompositeGraphic, not Graphic.");
    }
}
