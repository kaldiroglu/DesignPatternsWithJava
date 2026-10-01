package dev.kaldiroglu.dp.behavioral.command.gof.solution;

import java.util.ArrayList;
import java.util.List;

/** A list of menu items. Toolkit code: it knows labels and commands, and nothing else. */
public final class Menu {

    private final List<MenuItem> items = new ArrayList<>();

    public Menu add(MenuItem item) {
        items.add(item);
        return this;
    }

    public void click(String label) {
        items.stream()
                .filter(item -> item.label().equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no item labeled " + label))
                .clicked();
    }

    public List<String> labels() {
        return items.stream().map(MenuItem::label).toList();
    }
}
