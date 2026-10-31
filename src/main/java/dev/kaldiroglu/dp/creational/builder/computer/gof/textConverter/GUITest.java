package dev.kaldiroglu.dp.creational.ch04.computer.gof.textConverter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GUITest {
    public static void main(String[] args) {
        List<String> rtfText = List.of("Welcome", "to", "Design", "Patterns", "Course", "!");
        List<Token> tokens = new ArrayList<>();

        for (String word : rtfText) {
            tokens.add(new Token(TokenType.CHAR, word));
        }

        // 1. Create the specific Builder (The Representation)
        TextWidgetConverter widgetBuilder = new TextWidgetConverter();

        // 2. Pass it to the Director (The Process)
        RTFReader reader = new RTFReader(widgetBuilder);

        // 3. Run the construction process
        reader.parseRTF(tokens);

        // 4. Get the complex object (A fully styled Swing component)
        JTextPane visualWidget = widgetBuilder.getResult();

        // Wrap it in a window and show it
        JFrame frame = new JFrame("RTF Viewer");
        frame.add(new JScrollPane(visualWidget));
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}