package dev.kaldiroglu.dp.behavioral.strategy.hw.seating;

import java.util.List;

/**
 * The whole party in one row, or nothing.
 * <p>
 * The policy that shows why the interface returns a list and may return an empty one: this
 * algorithm can fail on a cabin the other two would happily seat, and that is a legitimate
 * answer rather than an exception.
 */
public final class KeepTogether implements SeatingPolicy {

    @Override
    public String name() {
        return "KEEP_TOGETHER";
    }

    @Override
    public List<String> allocate(SeatPlan plan, int partySize) {
        for (int row = 1; row <= plan.rows(); row++) {
            int finalRow = row;
            List<String> inRow = plan.free().stream()
                    .filter(seat -> plan.rowOf(seat) == finalRow)
                    .toList();
            if (inRow.size() >= partySize) {
                return inRow.subList(0, partySize);
            }
        }
        return List.of();
    }
}
