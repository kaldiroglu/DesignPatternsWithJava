package dev.kaldiroglu.dp.structural.adapter.gof;

/** The {@link Manipulator} a {@link LineShape} creates. */
public class LineManipulator extends Manipulator {

    private final Shape owner;

    public LineManipulator(Shape owner) {
        this.owner = owner;
    }

    @Override
    public void manipulate() {
        System.out.println("    [LineManipulator] manipulating " + owner);
    }
}
