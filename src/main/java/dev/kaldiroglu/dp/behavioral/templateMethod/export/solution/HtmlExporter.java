package dev.kaldiroglu.dp.behavioral.templateMethod.export.solution;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;

/** A <b>ConcreteClass</b> that also uses the hook: an HTML table must be closed. */
public final class HtmlExporter extends ReportExporter {

    public HtmlExporter(AuditLog audit) {
        super(audit);
    }

    @Override
    protected String header() {
        return "<table>\n<tr><th>customer</th><th>amount</th></tr>\n";
    }

    @Override
    protected String row(Sale sale) {
        return "<tr><td>" + sale.customer() + "</td><td>" + sale.amount() + "</td></tr>\n";
    }

    @Override
    protected String extension() {
        return ".html";
    }

    @Override
    protected String footer() {
        return "</table>\n";
    }
}
