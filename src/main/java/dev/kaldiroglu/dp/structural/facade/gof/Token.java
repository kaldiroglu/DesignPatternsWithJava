package dev.kaldiroglu.dp.structural.facade.gof;

/**
 * Subsystem class (GoF, p. 185): a lexical token produced by the {@link Scanner}.
 *
 * <p>Part of the compiler subsystem that the {@link Compiler} facade hides from
 * ordinary clients.</p>
 */
public record Token(Kind kind, String text) {

    public enum Kind {
        IDENT, INT, ASSIGN, PLUS, MINUS, RETURN, NEWLINE, EOF
    }

    public int asInt() {
        return Integer.parseInt(text);
    }

    @Override
    public String toString() {
        return kind + (text.isEmpty() ? "" : "('" + text + "')");
    }
}
