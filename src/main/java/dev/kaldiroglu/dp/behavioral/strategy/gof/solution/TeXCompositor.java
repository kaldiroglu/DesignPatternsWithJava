package dev.kaldiroglu.dp.behavioral.strategy.gof.solution;

import dev.kaldiroglu.dp.behavioral.strategy.gof.Component;
import dev.kaldiroglu.dp.behavioral.strategy.gof.Layout;

import java.util.ArrayList;
import java.util.List;

/**
 * A <b>ConcreteStrategy</b>: GoF's {@code TeXCompositor}, which "implements the TeX
 * algorithm for finding linebreaks. This strategy tries to optimize linebreaks globally,
 * that is, one paragraph at a time" (p. 316).
 * <p>
 * The whole paragraph is read before anything is decided. It first asks how many lines a
 * greedy pass needs, then finds the narrowest column that still fits in that many lines,
 * and lays the text out to <em>that</em> width. The result uses no more lines than the
 * greedy algorithm and spreads the slack across all of them instead of dumping it on the
 * last.
 * <p>
 * That is the point of GoF's example: two algorithms with the same job, the same inputs and
 * nothing in common inside — and a composition that cannot tell which it is holding.
 */
public final class TeXCompositor implements Compositor {

    @Override
    public String name() {
        return "TeXCompositor";
    }

    @Override
    public Layout compose(List<Component> components, int lineWidth) {
        int lines = greedy(components, lineWidth).size();
        int widest = components.stream().mapToInt(Component::width).max().orElse(1);

        // The narrowest column that still fits the paragraph into the same number of lines.
        int best = lineWidth;
        for (int width = widest; width <= lineWidth; width++) {
            if (greedy(components, width).size() <= lines) {
                best = width;
                break;
            }
        }
        return new Layout(greedy(components, best), lineWidth);
    }

    /** Fill each line until the next component will not fit — the pass TeX runs repeatedly. */
    private static List<List<Component>> greedy(List<Component> components, int lineWidth) {
        List<List<Component>> lines = new ArrayList<>();
        List<Component> line = new ArrayList<>();
        int used = 0;
        for (Component component : components) {
            int needed = line.isEmpty() ? component.width() : component.width() + 1;
            if (used + needed > lineWidth && !line.isEmpty() && line.getLast().breakable()) {
                lines.add(List.copyOf(line));
                line.clear();
                used = 0;
                needed = component.width();
            }
            line.add(component);
            used += needed;
        }
        lines.add(List.copyOf(line));
        return lines;
    }
}
