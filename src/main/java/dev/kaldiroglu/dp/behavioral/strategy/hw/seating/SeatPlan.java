package dev.kaldiroglu.dp.behavioral.strategy.hw.seating;

import java.util.List;

/**
 * The cabin: which seats exist, which are free, and what each one is like.
 *
 * @param rows      how many rows
 * @param perRow    seats per row, lettered from A
 * @param taken     seats already allocated
 */
public record SeatPlan(int rows, int perRow, List<String> taken) {

    public SeatPlan {
        taken = List.copyOf(taken);
    }

    public static SeatPlan empty(int rows, int perRow) {
        return new SeatPlan(rows, perRow, List.of());
    }

    public List<String> free() {
        return all().stream().filter(seat -> !taken.contains(seat)).toList();
    }

    public List<String> all() {
        return java.util.stream.IntStream.rangeClosed(1, rows)
                .boxed()
                .flatMap(row -> java.util.stream.IntStream.range(0, perRow)
                        .mapToObj(seat -> row + String.valueOf((char) ('A' + seat))))
                .toList();
    }

    /** A window seat is the first or last letter in its row. */
    public boolean isWindow(String seat) {
        char letter = seat.charAt(seat.length() - 1);
        return letter == 'A' || letter == (char) ('A' + perRow - 1);
    }

    public int rowOf(String seat) {
        return Integer.parseInt(seat.substring(0, seat.length() - 1));
    }

    public SeatPlan withTaken(List<String> seats) {
        List<String> now = new java.util.ArrayList<>(taken);
        now.addAll(seats);
        return new SeatPlan(rows, perRow, now);
    }
}
