package dev.kaldiroglu.dp.behavioral.strategy.hw.seating;

import java.util.List;

/** Whatever is free, in order. The cheapest fare gets this. */
public final class FirstAvailable implements SeatingPolicy {

    @Override
    public String name() {
        return "FIRST_AVAILABLE";
    }

    @Override
    public List<String> allocate(SeatPlan plan, int partySize) {
        List<String> free = plan.free();
        return free.size() < partySize ? List.of() : free.subList(0, partySize);
    }
}
