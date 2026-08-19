package dev.kaldiroglu.dp.behavioral.strategy.gof.solution;

import dev.kaldiroglu.dp.behavioral.strategy.gof.Component;
import dev.kaldiroglu.dp.behavioral.strategy.gof.Layout;

import java.util.ArrayList;
import java.util.List;

/**
 * A <b>ConcreteStrategy</b>: GoF's {@code SimpleCompositor}, "which implements a simple
 * line breaking strategy that determines linebreaks one at a time" (p. 316).
 * <p>
 * Fill the line until the next component will not fit, then break. Fast, and it leaves
 * whatever gap it leaves.
 */
public final class SimpleCompositor implements Compositor {

    @Override
    public String name() {
        return "SimpleCompositor";
    }

    @Override
    public Layout compose(List<Component> components, int lineWidth) {
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
        return new Layout(lines, lineWidth);
    }
}
