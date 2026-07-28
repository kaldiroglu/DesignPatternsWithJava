package dev.kaldiroglu.dp.structural.composite.graphic;

/** A Leaf: a circle has no children, and says so by not implementing {@link CompositeGraphic}. */
public class Circle extends GraphicObject {

    public Circle(String name, String color) {
        super(name, color);
    }

    @Override
    public void draw() {
        System.out.println("Drawing a circle: " + name);
    }

    @Override
    public void erase() {
        System.out.println("Erasing a circle: " + name);
    }

    @Override
    public void paint() {
        System.out.println("Painting a circle: " + name + ", color " + color);
    }
}
