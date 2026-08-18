package dev.kaldiroglu.dp.structural.bridge.retrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * A second RefinedAbstraction, added because the regulation was amended.
 * <p>
 * It stamps every row, and it names no engine. Written once, it is correct over the legacy
 * engine, over the one bought next year, and over any engine bought after that.
 */
public class AuditedReport extends RegulatoryReport {

    public AuditedReport(VendorClient engine) {
        super(engine);
    }

    @Override
    protected String statementFor(String period) {
        return "select ledger for quarter " + period + " with lineage";
    }

    @Override
    protected List<String> decorateRows(List<String> rows) {
        List<String> stamped = new ArrayList<>();
        for (String row : rows) {
            stamped.add(row + "  [audited]");
        }
        return stamped;
    }
}
