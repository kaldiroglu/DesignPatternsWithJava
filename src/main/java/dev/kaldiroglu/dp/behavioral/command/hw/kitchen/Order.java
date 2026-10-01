package dev.kaldiroglu.dp.behavioral.command.hw.kitchen;

/**
 * The <b>Command</b>: a ticket. The waiter writes it at the table, and the kitchen cooks it
 * whenever the rail reaches it. It is complete when it is written — dish, table, kitchen —
 * which is what lets it wait.
 */
public final class Order {

    private final Kitchen kitchen;
    private final String dish;
    private final int table;

    public Order(Kitchen kitchen, String dish, int table) {
        this.kitchen = kitchen;
        this.dish = dish;
        this.table = table;
    }

    public void execute() {
        kitchen.cook(dish, table);
    }

    public int table() {
        return table;
    }

    @Override
    public String toString() {
        return dish + " for table " + table;
    }
}
