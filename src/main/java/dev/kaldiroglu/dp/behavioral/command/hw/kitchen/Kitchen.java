package dev.kaldiroglu.dp.behavioral.command.hw.kitchen;

import java.util.ArrayList;
import java.util.List;

/** The <b>Receiver</b>: it cooks, in the order it is asked. */
public final class Kitchen {

    private final List<String> cooked = new ArrayList<>();

    public void cook(String dish, int table) {
        cooked.add(dish + " for table " + table);
    }

    public List<String> cooked() {
        return List.copyOf(cooked);
    }
}
