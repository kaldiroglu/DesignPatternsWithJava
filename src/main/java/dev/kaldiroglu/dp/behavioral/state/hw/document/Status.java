package dev.kaldiroglu.dp.behavioral.state.hw.document;

/** The statuses of a document. They hold no rules; the {@link Workflow} does. */
public enum Status {
    DRAFT, IN_REVIEW, PUBLISHED, ARCHIVED
}
