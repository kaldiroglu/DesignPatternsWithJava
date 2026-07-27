package dev.kaldiroglu.dp.structural.decorator.gof.visual;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps a paragraph into fixed-width lines. Shared by both designs so that the only
 * difference between the {@code problem} and {@code solution} packages is how
 * embellishments are attached, not how text is laid out.
 */
public final class TextLayout {

    private TextLayout() {
    }

    /** Greedily wraps {@code text} to {@code width} columns, padded and clipped to {@code height} rows. */
    public static List<String> wrap(String text, int width, int height) {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : text.split("\\s+")) {
            if (word.isEmpty()) {
                continue;
            }
            if (line.isEmpty()) {
                line.append(word);
            } else if (line.length() + 1 + word.length() <= width) {
                line.append(' ').append(word);
            } else {
                lines.add(line.toString());
                line = new StringBuilder(word);
            }
        }
        if (!line.isEmpty()) {
            lines.add(line.toString());
        }

        List<String> padded = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            String content = i < lines.size() ? lines.get(i) : "";
            if (content.length() > width) {
                content = content.substring(0, width);
            }
            padded.add(content + " ".repeat(width - content.length()));
        }
        return padded;
    }
}
