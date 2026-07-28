package dev.kaldiroglu.dp.structural.flyweight.forest.problem;

/**
 * Stands in for whatever reads a texture off disk, and counts how often it happens.
 *
 * <p>The count is the whole argument of this example. "It uses less memory" is an adjective;
 * "twenty thousand loads became two" is a number a test can assert.</p>
 */
public final class TextureLoader {

    private static int loads;
    private static long bytes;

    private TextureLoader() {
    }

    /** Copies the source bytes, as a real loader reading the file again would. */
    public static byte[] load(byte[] source) {
        loads++;
        bytes += source.length;
        return source.clone();
    }

    public static int loadCount() {
        return loads;
    }

    public static long bytesLoaded() {
        return bytes;
    }

    public static void reset() {
        loads = 0;
        bytes = 0;
    }
}
