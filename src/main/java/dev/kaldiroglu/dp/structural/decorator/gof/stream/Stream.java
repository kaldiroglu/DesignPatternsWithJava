package dev.kaldiroglu.dp.structural.decorator.gof.stream;

/**
 * The Component of GoF's second Decorator example (Sample Code, pp. 182–184).
 * <p>
 * A stream accepts values, accumulates them in a buffer, and calls
 * {@link #handleBufferFull()} when the buffer fills. That one overridable moment is
 * where every subclass and every decorator does its work: a file stream writes the
 * buffer out, a compressing decorator compresses it first.
 * <p>
 * The class is shared by the {@code problem} and {@code solution} packages so the two
 * designs are compared on identical behavior.
 */
public abstract class Stream {

    public static final int DEFAULT_BUFFER_SIZE = 64;

    private final StringBuilder buffer = new StringBuilder();
    private final int bufferSize;

    protected Stream(int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("buffer size must be positive");
        }
        this.bufferSize = bufferSize;
    }

    public final void putInt(int value) {
        putString(Integer.toString(value));
    }

    public final void putString(String value) {
        buffer.append(value);
        if (buffer.length() >= bufferSize) {
            handleBufferFull();
        }
    }

    /** Flushes whatever is left. Decorators must also close what they wrap. */
    public void close() {
        if (!buffer.isEmpty()) {
            handleBufferFull();
        }
    }

    /** Called when the buffer fills up, and once more on {@link #close()}. */
    protected abstract void handleBufferFull();

    /** Empties the buffer and returns what was in it. */
    protected final String takeBuffer() {
        String contents = buffer.toString();
        buffer.setLength(0);
        return contents;
    }
}
