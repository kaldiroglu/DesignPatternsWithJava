package dev.kaldiroglu.dp.structural.composite.graphic;

/** The roll-up: a circle answers 1, and a canvas adds up what its children answer. */
public class ShapeCountMain {

    public static void main(String[] args) {
        SampleDisplay sample = new SampleDisplay();

        System.out.println("shapes on the display: " + sample.display.shapeCount());
        System.out.println("shapes in the logo   : " + sample.logo.shapeCount());
        System.out.println("a single circle      : " + new Circle("c", "Red").shapeCount());
        System.out.println("  The client asked one object. Five shapes answered, two of them");
        System.out.println("  a level down, and nobody wrote a loop.");
    }
}
