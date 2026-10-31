package dev.kaldiroglu.dp.creational.ch04.computer.gof.textConverter;

// Representation 2: TeX (LaTeX) Format
public class TeXConverter implements TextConverter {
    private StringBuilder result = new StringBuilder();

    public void convertCharacter(char c) {
        result.append(c);
    }

    public void convertFontChange(String font) {
        result.append("\\font{" + font + "}");
    }

    public void convertParagraph() {
        result.append("\n\\par\n");
    }

    public String getResult() {
        return result.toString();
    }
}