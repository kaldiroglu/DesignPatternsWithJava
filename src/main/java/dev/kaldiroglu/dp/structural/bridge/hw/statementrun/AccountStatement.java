package dev.kaldiroglu.dp.structural.bridge.hw.statementrun;

import java.util.List;

/** A RefinedAbstraction: what moved, and where it left the balance. */
public final class AccountStatement extends Document {

    private final String account;
    private final String period;
    private final List<String[]> movements;
    private final String closing;

    public AccountStatement(Medium medium, String account, String period,
                            List<String[]> movements, String closing) {
        super(medium);
        this.account = account;
        this.period = period;
        this.movements = List.copyOf(movements);
        this.closing = closing;
    }

    @Override
    protected void body() {
        medium.heading(1, "Account statement");
        medium.field("Account", account);
        medium.field("Period", period);
        medium.heading(2, "Movements");
        movements.forEach(medium::row);
        medium.total("Closing balance", closing);
    }
}
