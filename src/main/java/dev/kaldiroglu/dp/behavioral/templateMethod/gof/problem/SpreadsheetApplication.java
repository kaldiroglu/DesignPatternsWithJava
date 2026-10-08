package dev.kaldiroglu.dp.behavioral.templateMethod.gof.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Before the pattern, second copy. The spreadsheet also wants to remember the last file it
 * opened, so it adds a step — in its own copy of the algorithm, in a place it chose.
 */
public final class SpreadsheetApplication {

    private final List<String> documents = new ArrayList<>();
    private final List<String> events = new ArrayList<>();

    public void openDocument(String name) {
        if (!name.endsWith(".sheet")) {
            return;
        }
        documents.add(name);
        events.add("remember " + name + " as the last file");
        events.add("open " + name);
        events.add("read cells from " + name);
    }

    public List<String> events() {
        return List.copyOf(events);
    }
}
