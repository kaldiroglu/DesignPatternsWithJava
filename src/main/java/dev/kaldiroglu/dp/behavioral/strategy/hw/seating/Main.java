package dev.kaldiroglu.dp.behavioral.strategy.hw.seating;

import java.util.List;

/**
 * Seats a party of two under three policies, and shows one policy refusing where another
 * succeeds.
 */
public class Main {

    public static void main(String[] args) {
        SeatPlan cabin = SeatPlan.empty(3, 4);
        BookingDesk desk = new BookingDesk(new FirstAvailable());

        SeatingPolicy[] policies = {new FirstAvailable(), new WindowPreferred(), new KeepTogether()};
        System.out.println("An empty cabin of 3 rows, seats A to D, and a party of two:");
        for (SeatingPolicy policy : policies) {
            desk.setPolicy(policy);
            System.out.println("  " + desk.policyName() + ": " + desk.seat(cabin, 2));
        }

        SeatPlan scattered = cabin.withTaken(
                List.of("1A", "1B", "1C", "2A", "2B", "2C", "3A", "3B", "3C", "3D"));
        System.out.println("Free seats left: " + scattered.free());
        System.out.println("  FIRST_AVAILABLE: " + new FirstAvailable().allocate(scattered, 2));
        System.out.println("  KEEP_TOGETHER: " + new KeepTogether().allocate(scattered, 2)
                + " (no row has two free seats)");
    }
}
