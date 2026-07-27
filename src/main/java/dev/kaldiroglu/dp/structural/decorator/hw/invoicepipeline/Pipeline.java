package dev.kaldiroglu.dp.structural.decorator.hw.invoicepipeline;

/**
 * The Component: something that can transform a block of bytes, and undo the transformation.
 * <p>
 * Declaring {@code undo} on the Component rather than only on the stages is what makes the
 * mirror-image rule structural instead of a convention. A chain built for writing can be run
 * backwards for reading, and no caller has to remember the order it was assembled in.
 */
public interface Pipeline {

    byte[] process(byte[] input);

    byte[] undo(byte[] input);
}
