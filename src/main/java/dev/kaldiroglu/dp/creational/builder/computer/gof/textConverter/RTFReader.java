package dev.kaldiroglu.dp.creational.ch04.computer.gof.textConverter;

import java.util.List;

public class RTFReader {
    private TextConverter builder;

    public RTFReader(TextConverter builder) {
        this.builder = builder;
    }

    // This mimics the 'ParseRTF' method in the book
    public void parseRTF(List<Token> tokens) {
        for (Token t : tokens) {
            switch (t.getType()) {
                case CHAR:
                    builder.convertCharacter(t.getChar());
                    break;
                case FONT:
                    builder.convertFontChange(t.getValue());
                    break;
                case PARAGRAPH:
                    builder.convertParagraph();
                    break;
            }
        }
    }
}