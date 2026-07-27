package dev.kaldiroglu.dp.structural.bridge.hw.statementrun;

import java.util.Objects;

/**
 * The Abstraction: a document that can be rendered onto any {@link Medium}.
 * <p>
 * It holds a medium and never asks which one it has. Three document kinds and three media
 * are six classes here, not nine — and a fourth medium is one class that no document knows
 * about.
 */
public abstract class Document {

    protected final Medium medium;

    protected Document(Medium medium) {
        this.medium = Objects.requireNonNull(medium);
    }

    /** Writes this document onto its medium and returns whatever the medium produced. */
    public final String render() {
        body();
        return medium.output();
    }

    /** The document's own content, expressed only in the medium's primitives. */
    protected abstract void body();
}
