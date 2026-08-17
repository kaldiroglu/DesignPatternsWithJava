package dev.kaldiroglu.dp.structural.flyweight.quote.problem;

import java.nio.charset.StandardCharsets;

/**
 * Decodes a field out of a network buffer, the way a real feed handler does.
 *
 * <p>This class exists to keep the example honest. Written with string literals, the problem
 * below appears to cost nothing: {@code "AAPL"} is interned by the compiler, so a million
 * ticks share one object and the memory the solution is supposed to save is already saved by
 * accident.</p>
 *
 * <p>A real handler does not receive literals. It receives bytes, and decoding them produces
 * a <b>fresh</b> {@code String} every time — which is why the problem is real in production
 * and invisible in a toy. Every decode here allocates, and every allocation is counted.</p>
 */
public final class Wire {

    private static int decoded;

    private Wire() {
    }

    /** Decodes one field. A new String object every call, exactly like the real thing. */
    public static String decode(String field) {
        decoded++;
        return new String(field.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

    public static int decodeCount() {
        return decoded;
    }

    public static void reset() {
        decoded = 0;
    }
}
