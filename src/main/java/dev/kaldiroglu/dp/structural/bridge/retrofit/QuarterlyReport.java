package dev.kaldiroglu.dp.structural.bridge.retrofit;

/**
 * A RefinedAbstraction. The required interface grows subtypes of its own, which is the half
 * of the argument an Adapter never has.
 */
public class QuarterlyReport extends RegulatoryReport {

    public QuarterlyReport(VendorClient engine) {
        super(engine);
    }

    @Override
    protected String statementFor(String period) {
        return "select ledger for quarter " + period;
    }
}
