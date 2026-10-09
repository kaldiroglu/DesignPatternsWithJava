package dev.kaldiroglu.dp.behavioral.memento.gof.problem;

/**
 * Undo by doing the opposite: move the box back and solve again.
 * <p>
 * The box returns to its place, but the solver's bend does not: the solver kept the bend it
 * chose during the move. To put it back, the command would need the solver's private
 * {@code bendX} — a getter and a setter that expose the solver's inside to every caller.
 */
public final class MoveCommand {

    private final ConstraintSolver solver;
    private final Graphic target;
    private final int dx;

    public MoveCommand(ConstraintSolver solver, Graphic target, int dx) {
        this.solver = solver;
        this.target = target;
        this.dx = dx;
    }

    public void execute() {
        target.move(dx);
        solver.solve();
    }

    public void unexecute() {
        target.move(-dx);
        solver.solve();
    }
}
