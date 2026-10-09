package dev.kaldiroglu.dp.behavioral.memento.gof.solution;

/**
 * The <b>Originator</b>: GoF's {@code ConstraintSolver}. It can write its state into a
 * memento and take it back, without showing that state to anyone else.
 */
public final class ConstraintSolver {

    /**
     * The <b>Memento</b>: GoF's {@code ConstraintSolverMemento}. Its field is private and it
     * has no methods, so a command can keep it but not read it. GoF implementation issue 1
     * (language support): C++ makes the solver a friend; Java nests the class.
     */
    public static final class Memento {
        private final int bendX;

        private Memento(int bendX) {
            this.bendX = bendX;
        }
    }

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

    public Memento createMemento() {
        return new Memento(bendX);
    }

    public void setMemento(Memento memento) {
        bendX = memento.bendX;
    }

    public String line() {
        return from.name() + " " + from.x() + " -> bend " + bendX + " -> " + to.name() + " " + to.x();
    }
}
