package dev.kaldiroglu.dp.creational.ch04.computer.gof.textConverter;

import javax.swing.*;
import javax.swing.text.*;

public class TextWidgetConverter implements TextConverter {
    // This is the "Product" being built
    private JTextPane textWidget;
    private StyledDocument doc;
    private MutableAttributeSet currentAttributes;

    public TextWidgetConverter() {
        this.textWidget = new JTextPane();
        this.doc = textWidget.getStyledDocument();
        this.currentAttributes = new SimpleAttributeSet();
    }

    @Override
    public void convertCharacter(char c) {
        try {
            // Logic to append character to the widget with current style
            doc.insertString(doc.getLength(), String.valueOf(c), currentAttributes);
        } catch (BadLocationException e) { e.printStackTrace(); }
    }

    @Override
    public void convertFontChange(String font) {
        // Logic to update the internal state of the widget builder
        StyleConstants.setFontFamily(currentAttributes, font);
    }

    @Override
    public void convertParagraph() {
        try {
            doc.insertString(doc.getLength(), "\n", currentAttributes);
        } catch (BadLocationException e) { e.printStackTrace(); }
    }

    // The method to retrieve the final "Complex Object"
    public JTextPane getResult() {
        return textWidget;
    }
}