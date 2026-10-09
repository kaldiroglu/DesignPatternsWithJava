package dev.kaldiroglu.dp.behavioral.chainOfResponsibility;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Helpers for the tests of this pattern: capture what a run prints, and read source code. */
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

    /** The source of a main class under src/main/java, with every comment removed. */
    public static String codeOf(String pathUnderBehavioral) {
        try {
            String text = Files.readString(Path.of(
                    "src/main/java/dev/kaldiroglu/dp/behavioral/" + pathUnderBehavioral));
            text = text.replaceAll("(?s)/\\*.*?\\*/", "");
            return text.replaceAll("//[^\\n]*", "");
        } catch (java.io.IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /** How many times the needle occurs in the text. */
    public static int countOf(String text, String needle) {
        int count = 0;
        for (int i = text.indexOf(needle); i >= 0; i = text.indexOf(needle, i + needle.length())) {
            count++;
        }
        return count;
    }
}
