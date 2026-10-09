package dev.kaldiroglu.dp.behavioral.strategy.hw.latefee;

/** Charges the same late book under three fee rules, changed on one returns desk. */
public class Main {

    public static void main(String[] args) {
        Loan tenDaysLate = new Loan("Design Patterns", 10, 50);
        ReturnsDesk desk = new ReturnsDesk(new StandardFee());

        FeeRule[] rules = {new StandardFee(), new CappedFee(300), new GraceThenDouble(3)};
        System.out.println("'" + tenDaysLate.title() + "', " + tenDaysLate.daysLate()
                + " days late, " + tenDaysLate.dailyRate() + " a day:");
        for (FeeRule rule : rules) {
            desk.setRule(rule);
            System.out.println("  " + desk.ruleName() + ": " + desk.charge(tenDaysLate));
        }

        Loan twoDaysLate = new Loan("Design Patterns", 2, 50);
        System.out.println("Two days late: STANDARD " + new StandardFee().charge(twoDaysLate)
                + ", GRACE_THEN_DOUBLE " + new GraceThenDouble(3).charge(twoDaysLate));
    }
}
