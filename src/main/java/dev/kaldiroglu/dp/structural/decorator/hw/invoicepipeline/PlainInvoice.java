package dev.kaldiroglu.dp.structural.decorator.hw.invoicepipeline;

/**
 * The ConcreteComponent: the invoice bytes, untouched.
 * <p>
 * It transforms nothing. Everything the pipeline does is added by decoration, which is why
 * this class never changed while the four requirements arrived.
 */
public final class PlainInvoice implements Pipeline {

    @Override
    public byte[] process(byte[] input) {
        return input.clone();
    }

    @Override
    public byte[] undo(byte[] input) {
        return input.clone();
    }
}
