package dev.kaldiroglu.dp.creational.ch04.computer.gof.textConverter;

// Representation 1: Plain ASCII
public class ASCIIConverter implements TextConverter {
    private StringBuilder result = new StringBuilder();

    public void convertCharacter(char c) {
        result.append(c);
    }

    public void convertFontChange(String font) { /* ASCII ignores fonts */ }

    public void convertParagraph() {
        result.append("\n");
    }

    public String getResult() {
        return result.toString();
    }
}