package dev.kaldiroglu.dp.structural.adapter.gof;

/** The {@link Manipulator} a {@link TextShape} creates (GoF p. 147). */
public class TextManipulator extends Manipulator {

    private final Shape owner;

    public TextManipulator(Shape owner) {
        this.owner = owner;
    }

    @Override
    public void manipulate() {
        System.out.println("    [TextManipulator] manipulating " + owner);
    }
}
