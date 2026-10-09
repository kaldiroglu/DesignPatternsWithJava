package dev.kaldiroglu.dp.behavioral.memento.gof.problem;

/**
 * Box A at 0, box B at 100, the bend at 50. Move B 60 to the left, past the bend, and undo
 * by moving it back: B returns to 100, but the bend stays at 20.
 */
public final class Main {

    public static void main(String[] args) {
        Graphic a = new Graphic("A", 0);
        Graphic b = new Graphic("B", 100);
        ConstraintSolver solver = new ConstraintSolver(a, b);
        MoveCommand move = new MoveCommand(solver, b, -60);

        System.out.println("At the start: " + solver.line());
        move.execute();
        System.out.println("After move:   " + solver.line());
        move.unexecute();
        System.out.println("After undo:   " + solver.line());
    }
}
