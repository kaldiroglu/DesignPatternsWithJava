package dev.kaldiroglu.dp.structural.composite.graphic;

/** A Leaf: a rectangle has no children, and says so by not implementing {@link CompositeGraphic}. */
public class Rectangle extends GraphicObject {

    public Rectangle(String name, String color) {
        super(name, color);
    }

    @Override
    public void draw() {
        System.out.println("Drawing a rectangle: " + name);
    }

    @Override
    public void erase() {
        System.out.println("Erasing a rectangle: " + name);
    }

    @Override
    public void paint() {
        System.out.println("Painting a rectangle: " + name + ", color " + color);
    }
}
