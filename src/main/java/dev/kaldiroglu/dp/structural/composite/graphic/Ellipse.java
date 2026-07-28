package dev.kaldiroglu.dp.structural.composite.graphic;

/** A Leaf: an ellipse has no children, and says so by not implementing {@link CompositeGraphic}. */
public class Ellipse extends GraphicObject {

    public Ellipse(String name, String color) {
        super(name, color);
    }

    @Override
    public void draw() {
        System.out.println("Drawing an ellipse: " + name);
    }

    @Override
    public void erase() {
        System.out.println("Erasing an ellipse: " + name);
    }

    @Override
    public void paint() {
        System.out.println("Painting an ellipse: " + name + ", color " + color);
    }
}
