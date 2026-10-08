package dev.kaldiroglu.dp.behavioral.templateMethod.export.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * The record the auditors read. Every export must add one line here, after the file is
 * made. This is the promise of the story.
 */
public final class AuditLog {

    private final List<String> lines = new ArrayList<>();

    public void record(User user, Export export, int rows) {
        lines.add(user.name() + " exported " + export.fileName() + " (" + rows + " rows)");
    }

    public List<String> lines() {
        return List.copyOf(lines);
    }
}
