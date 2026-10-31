package dev.kaldiroglu.dp.creational.ch04.computer.gof.textConverter;

public interface TextConverter {
    void convertCharacter(char c);

    void convertFontChange(String font);

    void convertParagraph();

    // In a real app, this would return the final product
    Object getResult();
}