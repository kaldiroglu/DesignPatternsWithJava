package dev.kaldiroglu.dp.behavioral.memento.gof.problem;

/**
 * GoF's motivation, before the pattern: a solver that keeps the line between two boxes.
 * <p>
 * The line has a bend at {@code bendX}. After a move, the solver keeps the bend where it is
 * if it still lies between the two boxes; otherwise it puts it in the middle. So the line
 * depends on the history of moves, not only on where the boxes are now — and moving a box
 * back does not always give the old line back.
 */
public final class ConstraintSolver {

    private final Graphic from;
    private final Graphic to;
    private int bendX;

    public ConstraintSolver(Graphic from, Graphic to) {
        this.from = from;
        this.to = to;
        this.bendX = (from.x() + to.x()) / 2;
    }

    public void solve() {
        int low = Math.min(from.x(), to.x());
        int high = Math.max(from.x(), to.x());
        if (bendX <= low || bendX >= high) {
            bendX = (low + high) / 2;
        }
    }

    public String line() {
        return from.name() + " " + from.x() + " -> bend " + bendX + " -> " + to.name() + " " + to.x();
    }
}
