package dev.kaldiroglu.dp.structural.composite.graphic;

/** A display with shapes on it, and a group nested inside. The demos here share it. */
final class SampleDisplay {

    final Canvas display = new Canvas("Display", "Light Green");
    final Canvas logo = new Canvas("Logo", "White");

    SampleDisplay() {
        display.addGraphic(new Circle("Red Circle", "Red"));
        display.addGraphic(new Circle("Blue Circle", "Blue"));
        display.addGraphic(new Ellipse("Black Ellipse", "Black"));

        // A canvas inside a canvas — the whole point of the solution.
        logo.addGraphic(new Triangle("Triangle", "Yellow"));
        logo.addGraphic(new Rectangle("Rectangle", "Green"));
        display.addGraphic(logo);
    }
}
