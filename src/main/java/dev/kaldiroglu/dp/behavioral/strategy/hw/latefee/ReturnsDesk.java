package dev.kaldiroglu.dp.behavioral.strategy.hw.latefee;

import java.util.Objects;

/** The Context: charges a returned loan under whichever rule the member's class carries. */
public final class ReturnsDesk {

    private FeeRule rule;

    public ReturnsDesk(FeeRule rule) {
        this.rule = Objects.requireNonNull(rule);
    }

    public void setRule(FeeRule rule) {
        this.rule = Objects.requireNonNull(rule);
    }

    public String ruleName() {
        return rule.name();
    }

    public int charge(Loan loan) {
        return rule.charge(loan);
    }
}
