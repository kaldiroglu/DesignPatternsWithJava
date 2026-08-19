package dev.kaldiroglu.dp.behavioral.strategy.hw.seating;

import java.util.List;

/**
 * The Strategy: how a booking of {@code partySize} people is seated.
 * <p>
 * Homework 1. Three policies are supplied; the exercise is a fourth, and the harder question
 * of whether the interface should have been about seats at all.
 */
public interface SeatingPolicy {

    String name();

    /** Seats for one booking, or an empty list when this policy cannot serve it. */
    List<String> allocate(SeatPlan plan, int partySize);
}
