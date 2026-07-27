package dev.kaldiroglu.dp.structural.bridge.hw.statementrun;

/** A RefinedAbstraction: a polite letter about an unpaid invoice. */
public final class DunningLetter extends Document {

    private final String customer;
    private final String invoiceNumber;
    private final String amount;
    private final int daysOverdue;

    public DunningLetter(Medium medium, String customer, String invoiceNumber,
                         String amount, int daysOverdue) {
        super(medium);
        this.customer = customer;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.daysOverdue = daysOverdue;
    }

    @Override
    protected void body() {
        medium.heading(1, "Payment reminder");
        medium.field("Customer", customer);
        medium.field("Invoice", invoiceNumber);
        medium.field("Days overdue", String.valueOf(daysOverdue));
        medium.total("Amount outstanding", amount);
    }
}
