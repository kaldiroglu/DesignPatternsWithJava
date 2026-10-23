package dev.kaldiroglu.dp.behavioral.iterator.gof.solution;

/**
 * An internal iterator: the traverser runs the loop, and PrintNEmployees stops it after two.
 */
public final class InternalIteratorMain {

    public static void main(String[] args) {
        PrintNEmployees firstTwo = new PrintNEmployees(SampleLists.list(), 2);
        boolean finished = firstTwo.traverse();
        System.out.println("First two: " + firstTwo.lines() + ", walked to the end: " + finished);
    }
}
