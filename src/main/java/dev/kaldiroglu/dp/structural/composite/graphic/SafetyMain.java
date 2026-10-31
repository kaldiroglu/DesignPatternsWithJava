package dev.kaldiroglu.dp.structural.composite.graphic;

/**
 * What the client has to know. Building the tree needs a {@link CompositeGraphic}; drawing
 * it needs only a {@link Graphic}. That split is the cost of keeping {@code addGraphic} off
 * the Component, and the compile error it prints about is what it buys.
 */
public class SafetyMain {

    public static void main(String[] args) {
        Canvas display = new SampleDisplay().display;

        Graphic asComponent = display;   // a Canvas is a Graphic, so this needs no cast
        asComponent.draw();

        System.out.println();
        System.out.println("A leaf cannot be given children, and the compiler says so:");
        System.out.println("    new Circle(\"c\", \"Red\").addGraphic(..)   does not compile");
        System.out.println("That is the safety this package chose. Its price is that a client");
        System.out.println("building a tree must hold CompositeGraphic, not Graphic.");
    }
}
