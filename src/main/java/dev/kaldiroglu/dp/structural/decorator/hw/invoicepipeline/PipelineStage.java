package dev.kaldiroglu.dp.structural.decorator.hw.invoicepipeline;

import java.util.Objects;

/**
 * The Decorator: one stage of the pipeline.
 * <p>
 * It is a {@link Pipeline} and it holds a {@link Pipeline}. Note how {@code process} and
 * {@code undo} are deliberately not symmetric in their nesting: on the way out this stage
 * runs <em>after</em> the inner one, and on the way back it runs <em>before</em>. That single
 * asymmetry is what guarantees the read chain is the exact mirror of the write chain.
 */
public abstract class PipelineStage implements Pipeline {

    protected final Pipeline inner;

    protected PipelineStage(Pipeline inner) {
        this.inner = Objects.requireNonNull(inner, "a stage must wrap something");
    }

    /** What this stage adds on the way out. */
    protected abstract byte[] forward(byte[] bytes);

    /** What this stage removes on the way back. */
    protected abstract byte[] backward(byte[] bytes);

    @Override
    public final byte[] process(byte[] input) {
        return forward(inner.process(input));
    }

    @Override
    public final byte[] undo(byte[] input) {
        return inner.undo(backward(input));
    }
}
