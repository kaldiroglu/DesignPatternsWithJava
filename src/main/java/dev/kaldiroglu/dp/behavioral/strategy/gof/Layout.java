package dev.kaldiroglu.dp.behavioral.strategy.gof;

import java.util.List;

/**
 * The result of laying components out: which components landed on which line.
 *
 * @param lines   the components, grouped by the line they were placed on
 * @param lineWidth the column width they were fitted into
 */
public record Layout(List<List<Component>> lines, int lineWidth) {

    public Layout {
        lines = List.copyOf(lines);
    }

    public int lineCount() {
        return lines.size();
    }

    /** How wide the text on one line came out. */
    public int widthOf(int line) {
        return lines.get(line).stream().mapToInt(Component::width).sum()
                + Math.max(0, lines.get(line).size() - 1);   // one space between components
    }

    /** The room left over on one line — what a good break minimizes. */
    public int slackOn(int line) {
        return lineWidth - widthOf(line);
    }

    /** The worst gap left on any line but the last, which is where bad breaks show. */
    public int worstSlack() {
        int worst = 0;
        for (int i = 0; i < lines.size() - 1; i++) {
            worst = Math.max(worst, slackOn(i));
        }
        return worst;
    }

    /** The text as it would be read, one line per entry. */
    public List<String> render() {
        return lines.stream()
                .map(line -> String.join(" ", line.stream().map(Component::text).toList()))
                .toList();
    }
}
