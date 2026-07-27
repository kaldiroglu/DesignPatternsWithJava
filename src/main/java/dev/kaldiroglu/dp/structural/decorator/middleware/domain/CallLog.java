package dev.kaldiroglu.dp.structural.decorator.middleware.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Where log lines go. A real system would hand these to a logging framework; collecting
 * them in a list lets the tests assert what was logged, and in what order — which is how
 * the order of decorators becomes visible.
 */
public final class CallLog {

    private final List<String> lines = new ArrayList<>();
    private final boolean echo;

    public CallLog() {
        this(false);
    }

    public CallLog(boolean echo) {
        this.echo = echo;
    }

    public void record(String line) {
        lines.add(line);
        if (echo) {
            System.out.println("    log | " + line);
        }
    }

    public List<String> lines() {
        return List.copyOf(lines);
    }

    public int size() {
        return lines.size();
    }

    public void clear() {
        lines.clear();
    }
}
