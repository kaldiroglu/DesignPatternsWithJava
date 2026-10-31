package dev.kaldiroglu.dp.creational.ch04.computer.gof.textConverter;

public class Token {
    private TokenType type;
    private String value;

    public Token(TokenType t, String v) {
        this.type = t;
        this.value = v;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public char getChar() {
        return value.charAt(0);
    }
}