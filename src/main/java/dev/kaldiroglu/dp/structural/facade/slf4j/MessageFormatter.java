package dev.kaldiroglu.dp.structural.facade.slf4j;

/**
 * Subsystem helper that expands SLF4J-style <code>{}</code> placeholders, e.g.
 * {@code format("user {} sent {} messages", "john", 3)} yields
 * {@code "user john sent 3 messages"}. The real SLF4J equivalent is
 * {@code org.slf4j.helpers.MessageFormatter}.
 */
public final class MessageFormatter {

    private static final String PLACEHOLDER = "{}";

    private MessageFormatter() {
    }

    /** Substitutes each <code>{}</code> in {@code pattern} with the next argument, in order. */
    public static String format(String pattern, Object... arguments) {
        if (pattern == null || arguments == null || arguments.length == 0) {
            return pattern;
        }

        StringBuilder result = new StringBuilder(pattern.length() + 16 * arguments.length);
        int cursor = 0;
        int argIndex = 0;
        while (argIndex < arguments.length) {
            int placeholder = pattern.indexOf(PLACEHOLDER, cursor);
            if (placeholder < 0) {
                break; // more arguments than placeholders: ignore the extras
            }
            result.append(pattern, cursor, placeholder).append(String.valueOf(arguments[argIndex]));
            cursor = placeholder + PLACEHOLDER.length();
            argIndex++;
        }
        result.append(pattern, cursor, pattern.length());
        return result.toString();
    }
}
