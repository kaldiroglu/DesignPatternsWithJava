package dev.kaldiroglu.dp.structural.facade.gof;

import java.util.ArrayList;
import java.util.List;

/**
 * Subsystem class (GoF, p. 185): the lexical analyzer. It turns raw source text
 * into a stream of {@link Token}s that the {@link Parser} consumes.
 *
 * <p>This is one of the low-level classes that a client would otherwise have to
 * know about and wire up by hand — the job the {@link Compiler} facade removes.</p>
 */
public class Scanner {

    private final List<Token> tokens = new ArrayList<>();
    private int position;

    public Scanner(String source) {
        tokenize(source);
    }

    /** Returns the next token and advances. */
    public Token next() {
        Token token = peek();
        if (position < tokens.size() - 1) {
            position++;
        }
        return token;
    }

    /** Returns the next token without consuming it. */
    public Token peek() {
        return tokens.get(position);
    }

    private void tokenize(String source) {
        int i = 0;
        int n = source.length();
        while (i < n) {
            char c = source.charAt(i);
            if (c == '\n') {
                tokens.add(new Token(Token.Kind.NEWLINE, ""));
                i++;
            } else if (c == ' ' || c == '\t' || c == '\r') {
                i++;
            } else if (Character.isDigit(c)) {
                int start = i;
                while (i < n && Character.isDigit(source.charAt(i))) {
                    i++;
                }
                tokens.add(new Token(Token.Kind.INT, source.substring(start, i)));
            } else if (Character.isLetter(c)) {
                int start = i;
                while (i < n && Character.isLetterOrDigit(source.charAt(i))) {
                    i++;
                }
                String word = source.substring(start, i);
                tokens.add(word.equals("return")
                        ? new Token(Token.Kind.RETURN, "return")
                        : new Token(Token.Kind.IDENT, word));
            } else if (c == '=') {
                tokens.add(new Token(Token.Kind.ASSIGN, "="));
                i++;
            } else if (c == '+') {
                tokens.add(new Token(Token.Kind.PLUS, "+"));
                i++;
            } else if (c == '-') {
                tokens.add(new Token(Token.Kind.MINUS, "-"));
                i++;
            } else {
                throw new IllegalArgumentException("Unexpected character: '" + c + "'");
            }
        }
        tokens.add(new Token(Token.Kind.EOF, ""));
    }
}
