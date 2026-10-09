package dev.kaldiroglu.dp.behavioral.visitor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

/** Helper for the visitor tests: several examples print to standard output. */
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

    /** The text of a Java source file with its comments removed. */
    public static String codeOf(String path) {
        try {
            String text = java.nio.file.Files.readString(java.nio.file.Path.of(path));
            return text.replaceAll("(?s)/\\*.*?\\*/", " ").replaceAll("//[^\n]*", " ");
        } catch (java.io.IOException e) {
            throw new java.io.UncheckedIOException(e);
        }
    }
}
