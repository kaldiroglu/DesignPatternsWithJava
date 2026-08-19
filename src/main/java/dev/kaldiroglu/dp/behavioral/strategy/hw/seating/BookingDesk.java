package dev.kaldiroglu.dp.behavioral.strategy.hw.seating;

import java.util.List;
import java.util.Objects;

/** The Context: holds a policy and seats a booking with it. */
public final class BookingDesk {

    private SeatingPolicy policy;

    public BookingDesk(SeatingPolicy policy) {
        this.policy = Objects.requireNonNull(policy);
    }

    public void setPolicy(SeatingPolicy policy) {
        this.policy = Objects.requireNonNull(policy);
    }

    public String policyName() {
        return policy.name();
    }

    public List<String> seat(SeatPlan plan, int partySize) {
        return policy.allocate(plan, partySize);
    }
}
