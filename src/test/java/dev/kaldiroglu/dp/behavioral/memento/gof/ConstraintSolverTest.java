package dev.kaldiroglu.dp.behavioral.memento.gof;

import dev.kaldiroglu.dp.behavioral.memento.gof.solution.ConstraintSolver;
import dev.kaldiroglu.dp.behavioral.memento.gof.solution.Graphic;
import dev.kaldiroglu.dp.behavioral.memento.gof.solution.MoveCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static dev.kaldiroglu.dp.behavioral.memento.Printed.by;
import static org.junit.jupiter.api.Assertions.*;

/** GoF's constraint solver: A at 0, B at 100, the bend at 50; B moves 60 to the left. */
class ConstraintSolverTest {

    @Test
    @DisplayName("before the pattern: the bend goes 50, 20, and stays at 20 after undo")
    void undoByMovingBack() {
        var a = new dev.kaldiroglu.dp.behavioral.memento.gof.problem.Graphic("A", 0);
        var b = new dev.kaldiroglu.dp.behavioral.memento.gof.problem.Graphic("B", 100);
        var solver = new dev.kaldiroglu.dp.behavioral.memento.gof.problem.ConstraintSolver(a, b);
        var move = new dev.kaldiroglu.dp.behavioral.memento.gof.problem.MoveCommand(solver, b, -60);

        assertEquals("A 0 -> bend 50 -> B 100", solver.line());
        move.execute();
        assertEquals("A 0 -> bend 20 -> B 40", solver.line());
        move.unexecute();
        assertEquals("A 0 -> bend 20 -> B 100", solver.line(), "B is back, the bend is not");
    }

    @Test
    @DisplayName("with a memento: the bend goes 50, 20, and is at 50 again after undo")
    void undoWithAMemento() {
        Graphic a = new Graphic("A", 0);
        Graphic b = new Graphic("B", 100);
        ConstraintSolver solver = new ConstraintSolver(a, b);
        MoveCommand move = new MoveCommand(solver, b, -60);

        assertEquals("A 0 -> bend 50 -> B 100", solver.line());
        move.execute();
        assertEquals("A 0 -> bend 20 -> B 40", solver.line());
        move.unexecute();
        assertEquals("A 0 -> bend 50 -> B 100", solver.line());
    }

    @Test
    @DisplayName("two moves and two undos: given back in reverse order the bend is right, in another order it is wrong")
    void twoMovesTwoUndos() {
        assertEquals("A 0 -> bend 50 -> B 100", afterTwoMovesAndUndos(true));
        assertEquals("A 0 -> bend 20 -> B 100", afterTwoMovesAndUndos(false));
    }

    private static String afterTwoMovesAndUndos(boolean reverseOrder) {
        Graphic a = new Graphic("A", 0);
        Graphic b = new Graphic("B", 100);
        ConstraintSolver solver = new ConstraintSolver(a, b);
        MoveCommand moveB = new MoveCommand(solver, b, -60);
        MoveCommand moveA = new MoveCommand(solver, a, 30);
        moveB.execute();
        moveA.execute();
        if (reverseOrder) {
            moveA.unexecute();
            moveB.unexecute();
        } else {
            moveB.unexecute();
            moveA.unexecute();
        }
        return solver.line();
    }

    @Test
    @DisplayName("the memento's field and constructor are private, and it has no methods")
    void theMementoIsClosed() {
        Class<ConstraintSolver.Memento> type = ConstraintSolver.Memento.class;
        for (Field field : type.getDeclaredFields()) {
            assertTrue(Modifier.isPrivate(field.getModifiers()), field.getName());
        }
        for (Constructor<?> constructor : type.getDeclaredConstructors()) {
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        }
        assertEquals(0, java.util.Arrays.stream(type.getDeclaredMethods())
                .filter(m -> !m.isSynthetic()).count());
    }

    @Test
    @DisplayName("Main prints the bend at each step in both designs")
    void mainOutput() {
        assertEquals(List.of(
                "Before the pattern",
                "  at the start: A 0 -> bend 50 -> B 100",
                "  after move:   A 0 -> bend 20 -> B 40",
                "  after undo:   A 0 -> bend 20 -> B 100",
                "With a memento",
                "  at the start: A 0 -> bend 50 -> B 100",
                "  after move:   A 0 -> bend 20 -> B 40",
                "  after undo:   A 0 -> bend 50 -> B 100"), by(() -> Main.main(new String[0])));
    }
}
