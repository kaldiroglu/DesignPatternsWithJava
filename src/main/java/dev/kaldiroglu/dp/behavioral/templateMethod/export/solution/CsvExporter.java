package dev.kaldiroglu.dp.behavioral.templateMethod.export.solution;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;

/** A <b>ConcreteClass</b>: only the text of a CSV file. It has no {@code export()} of its own. */
public final class CsvExporter extends ReportExporter {

    public CsvExporter(AuditLog audit) {
        super(audit);
    }

    @Override
    protected String header() {
        return "customer,amount\n";
    }

    @Override
    protected String row(Sale sale) {
        return sale.customer() + "," + sale.amount() + "\n";
    }

    @Override
    protected String extension() {
        return ".csv";
    }
}
