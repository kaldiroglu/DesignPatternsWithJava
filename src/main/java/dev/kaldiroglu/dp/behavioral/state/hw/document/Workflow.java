package dev.kaldiroglu.dp.behavioral.state.hw.document;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * Homework 2: the transitions in one central place.
 * <p>
 * The original deck describes two ways to manage transitions: the states decide (as in
 * {@code order.solution}), or a central object decides. This is the second way. The whole
 * life of a document is one table, so a reviewer can read every rule on one screen, and a
 * new rule is one line.
 * <p>
 * The cost: the statuses are only names. A status that needs its own behavior or data —
 * like {@code order.solution.Shipped} — does not fit a table.
 */
public final class Workflow {

    private final Map<Status, Map<Action, Status>> next = new EnumMap<>(Status.class);

    public Workflow() {
        allow(Status.DRAFT, Action.SUBMIT, Status.IN_REVIEW);
        allow(Status.IN_REVIEW, Action.APPROVE, Status.PUBLISHED);
        allow(Status.IN_REVIEW, Action.REJECT, Status.DRAFT);
        allow(Status.PUBLISHED, Action.ARCHIVE, Status.ARCHIVED);
    }

    private void allow(Status from, Action action, Status to) {
        next.computeIfAbsent(from, s -> new EnumMap<>(Action.class)).put(action, to);
    }

    public Optional<Status> after(Status from, Action action) {
        return Optional.ofNullable(next.getOrDefault(from, Map.of()).get(action));
    }
}
