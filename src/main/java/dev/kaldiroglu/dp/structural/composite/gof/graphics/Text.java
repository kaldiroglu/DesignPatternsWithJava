package dev.kaldiroglu.dp.structural.composite.gof.graphics;

/**
 * Leaf role of the Composite solution (GoF, p. 165) — a primitive graphic.
 *
 * <p>A run of text on the canvas. Childless, like the other primitives.</p>
 */
public class Text extends Graphic {

    private final String content;

    public Text(String content) {
        this.content = content;
    }

    public String content() {
        return content;
    }

    @Override
    public void draw(Point at) {
        System.out.println("Text \"" + content + "\" drawn at " + at);
    }
}
