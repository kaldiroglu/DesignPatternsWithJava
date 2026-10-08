package dev.kaldiroglu.dp.behavioral.state.hw.document;

/** The context. It asks the central {@link Workflow} for its next status. */
public final class Document {

    private final Workflow workflow;
    private Status status = Status.DRAFT;

    public Document(Workflow workflow) {
        this.workflow = workflow;
    }

    public void apply(Action action) {
        status = workflow.after(status, action).orElseThrow(() -> new IllegalStateException(
                "cannot " + action.name().toLowerCase() + " a " + status.name().toLowerCase()
                        + " document"));
    }

    public Status status() {
        return status;
    }
}
