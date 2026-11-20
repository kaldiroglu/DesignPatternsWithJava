package dev.kaldiroglu.dp.behavioral.state.order.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Prints the order's finite state machine as a table, read from the state classes.
 * <p>
 * A row is a state, a column is an event, and a cell is the state the order is in after
 * that event. A dash means the state refuses the event. Nothing in the table is written by
 * hand: each cell puts a new order into the row's state, sends the column's event, and
 * reads the state that comes back.
 */
public final class TransitionTableMain {

    static final List<String> STATES = List.of("placed", "paid", "shipped", "delivered", "cancelled");
    static final List<String> EVENTS = List.of("pay", "ship", "failDelivery", "deliver", "cancel");

    public static void main(String[] args) {
        List<List<String>> table = new ArrayList<>();
        List<String> header = new ArrayList<>(List.of(""));
        header.addAll(EVENTS);
        table.add(header);
        for (String state : STATES) {
            List<String> row = new ArrayList<>(List.of(state));
            for (String event : EVENTS) {
                row.add(cell(state, event));
            }
            table.add(row);
        }
        print(table);
    }

    /**
     * The next state after the event, or "-" when it is refused. When the event keeps the
     * order in the same state, it is sent again, to find the attempt that moves it on.
     */
    static String cell(String state, String event) {
        Order order = orderIn(state);
        try {
            send(order, event);
            if (!order.state().name().equals(state)) {
                return order.state().name();
            }
            for (int attempt = 2; attempt <= Shipped.MAX_ATTEMPTS; attempt++) {
                send(order, event);
                if (!order.state().name().equals(state)) {
                    return state + "; " + order.state().name() + " on attempt " + attempt;
                }
            }
            return state;
        } catch (IllegalStateException refused) {
            return "-";
        }
    }

    /** A new order, moved into the named state through its own events. */
    static Order orderIn(String state) {
        Order order = new Order();
        switch (state) {
            case "placed" -> { }
            case "paid" -> order.pay();
            case "shipped" -> { order.pay(); order.ship("TR-1"); }
            case "delivered" -> { order.pay(); order.ship("TR-1"); order.deliver(); }
            case "cancelled" -> order.cancel();
            default -> throw new IllegalArgumentException("no state " + state);
        }
        return order;
    }

    private static void send(Order order, String event) {
        switch (event) {
            case "pay" -> order.pay();
            case "ship" -> order.ship("TR-1");
            case "failDelivery" -> order.failDelivery();
            case "deliver" -> order.deliver();
            case "cancel" -> order.cancel();
            default -> throw new IllegalArgumentException("no event " + event);
        }
    }

    private static void print(List<List<String>> table) {
        int columns = table.getFirst().size();
        int[] widths = new int[columns];
        for (List<String> row : table) {
            for (int c = 0; c < columns; c++) {
                widths[c] = Math.max(widths[c], row.get(c).length());
            }
        }
        for (List<String> row : table) {
            StringBuilder line = new StringBuilder();
            for (int c = 0; c < columns; c++) {
                line.append(String.format("%-" + (widths[c] + 2) + "s", row.get(c)));
            }
            System.out.println(line.toString().stripTrailing());
        }
    }
}
