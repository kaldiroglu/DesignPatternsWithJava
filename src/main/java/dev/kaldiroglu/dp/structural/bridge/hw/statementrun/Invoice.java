package dev.kaldiroglu.dp.structural.bridge.hw.statementrun;

import java.util.List;

/** A RefinedAbstraction: what we are owed, and for what. */
public final class Invoice extends Document {

    private final String number;
    private final String customer;
    private final List<String[]> lines;
    private final String total;

    public Invoice(Medium medium, String number, String customer,
                   List<String[]> lines, String total) {
        super(medium);
        this.number = number;
        this.customer = customer;
        this.lines = List.copyOf(lines);
        this.total = total;
    }

    @Override
    protected void body() {
        medium.heading(1, "Invoice " + number);
        medium.field("Customer", customer);
        medium.heading(2, "Items");
        lines.forEach(medium::row);
        medium.total("Amount due", total);
    }
}
