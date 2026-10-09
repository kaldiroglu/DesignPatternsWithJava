package dev.kaldiroglu.dp.behavioral.command.lender;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/** Helpers for the lender tests: the lender classes print to standard output. */
public final class Printed {

    private Printed() {
    }

    /** Runs the action and returns what it printed, one line per element. */
    public static List<String> by(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString().lines().toList();
    }

    /** The parameter types of a class's lend method. */
    public static List<Class<?>> lendParameters(Class<?> lender) {
        Method lend = Arrays.stream(lender.getDeclaredMethods())
                .filter(m -> m.getName().equals("lend"))
                .findFirst()
                .orElseThrow();
        return List.of(lend.getParameterTypes());
    }
}
