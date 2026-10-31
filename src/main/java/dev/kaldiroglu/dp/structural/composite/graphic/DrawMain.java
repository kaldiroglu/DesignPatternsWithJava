package dev.kaldiroglu.dp.structural.composite.graphic;

/** Lists the tree, then draws it with one call to the root. */
public class DrawMain {

    public static void main(String[] args) {
        Canvas display = new SampleDisplay().display;

        display.listGraphic();
        System.out.println("****************");
        display.draw();
        System.out.println("****************");
    }
}
