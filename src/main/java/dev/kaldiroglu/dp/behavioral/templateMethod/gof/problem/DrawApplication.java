package dev.kaldiroglu.dp.behavioral.templateMethod.gof.problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Before the pattern: each application writes the whole of opening a document.
 * <p>
 * GoF's framework has many applications — a drawing program, a spreadsheet — and opening a
 * document is the same five steps in all of them: check the file, create the document, add
 * it to the list, open it, read it. Here each application has its own copy. Compare
 * {@link SpreadsheetApplication}: the same steps, in the same order, with two of them
 * different.
 */
public final class DrawApplication {

    private final List<String> documents = new ArrayList<>();
    private final List<String> events = new ArrayList<>();

    public void openDocument(String name) {
        if (!name.endsWith(".draw")) {
            return;
        }
        documents.add(name);
        events.add("open " + name);
        events.add("read shapes from " + name);
    }

    public List<String> events() {
        return List.copyOf(events);
    }
}
