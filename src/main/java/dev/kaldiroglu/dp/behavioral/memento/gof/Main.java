package dev.kaldiroglu.dp.behavioral.memento.gof;

/**
 * Box A at 0, box B at 100, the bend at 50. Move B 60 to the left — past the bend — and undo.
 */
public final class Main {

    public static void main(String[] args) {
        var a1 = new dev.kaldiroglu.dp.behavioral.memento.gof.problem.Graphic("A", 0);
        var b1 = new dev.kaldiroglu.dp.behavioral.memento.gof.problem.Graphic("B", 100);
        var solver1 = new dev.kaldiroglu.dp.behavioral.memento.gof.problem.ConstraintSolver(a1, b1);
        var move1 = new dev.kaldiroglu.dp.behavioral.memento.gof.problem.MoveCommand(solver1, b1, -60);
        System.out.println("Before the pattern");
        System.out.println("  at the start: " + solver1.line());
        move1.execute();
        System.out.println("  after move:   " + solver1.line());
        move1.unexecute();
        System.out.println("  after undo:   " + solver1.line());

        var a2 = new dev.kaldiroglu.dp.behavioral.memento.gof.solution.Graphic("A", 0);
        var b2 = new dev.kaldiroglu.dp.behavioral.memento.gof.solution.Graphic("B", 100);
        var solver2 = new dev.kaldiroglu.dp.behavioral.memento.gof.solution.ConstraintSolver(a2, b2);
        var move2 = new dev.kaldiroglu.dp.behavioral.memento.gof.solution.MoveCommand(solver2, b2, -60);
        System.out.println("With a memento");
        System.out.println("  at the start: " + solver2.line());
        move2.execute();
        System.out.println("  after move:   " + solver2.line());
        move2.unexecute();
        System.out.println("  after undo:   " + solver2.line());
    }
}
