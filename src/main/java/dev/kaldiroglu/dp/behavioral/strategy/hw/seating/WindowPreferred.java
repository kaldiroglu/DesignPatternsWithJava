package dev.kaldiroglu.dp.behavioral.strategy.hw.seating;

import java.util.ArrayList;
import java.util.List;

/** Windows first, then anything. What a frequent flyer is given. */
public final class WindowPreferred implements SeatingPolicy {

    @Override
    public String name() {
        return "WINDOW_PREFERRED";
    }

    @Override
    public List<String> allocate(SeatPlan plan, int partySize) {
        List<String> free = plan.free();
        if (free.size() < partySize) {
            return List.of();
        }
        List<String> chosen = new ArrayList<>(free.stream().filter(plan::isWindow).toList());
        free.stream().filter(seat -> !plan.isWindow(seat)).forEach(chosen::add);
        return List.copyOf(chosen.subList(0, partySize));
    }
}
