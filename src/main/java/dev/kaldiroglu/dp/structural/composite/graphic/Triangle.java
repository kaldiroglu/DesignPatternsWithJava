package dev.kaldiroglu.dp.structural.composite.graphic;

/** A Leaf: a triangle has no children, and says so by not implementing {@link CompositeGraphic}. */
public class Triangle extends GraphicObject {

    public Triangle(String name, String color) {
        super(name, color);
    }

    @Override
    public void draw() {
        System.out.println("Drawing a triangle: " + name);
    }

    @Override
    public void erase() {
        System.out.println("Erasing a triangle: " + name);
    }

    @Override
    public void paint() {
        System.out.println("Painting a triangle: " + name + ", color " + color);
    }
}
