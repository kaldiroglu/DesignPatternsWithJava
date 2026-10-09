package dev.kaldiroglu.dp.behavioral.memento.gof.solution;

/**
 * The <b>Caretaker</b>: GoF's {@code MoveCommand}. Before it moves the box, it asks the
 * solver for a memento; to undo, it moves the box back and gives the memento back.
 */
public final class MoveCommand {

    private final ConstraintSolver solver;
    private final Graphic target;
    private final int dx;
    private ConstraintSolver.Memento state;

    public MoveCommand(ConstraintSolver solver, Graphic target, int dx) {
        this.solver = solver;
        this.target = target;
        this.dx = dx;
    }

    public void execute() {
        state = solver.createMemento();
        target.move(dx);
        solver.solve();
    }

    public void unexecute() {
        target.move(-dx);
        solver.setMemento(state);
        solver.solve();
    }
}
