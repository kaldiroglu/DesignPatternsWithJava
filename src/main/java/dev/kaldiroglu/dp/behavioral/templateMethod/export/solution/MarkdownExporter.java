package dev.kaldiroglu.dp.behavioral.templateMethod.export.solution;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;

/**
 * A <b>ConcreteClass</b> added later, by the same new team member as in stage three.
 * <p>
 * This time there is nothing to forget. The audit line is written by the template method,
 * and a subclass cannot override it, because {@code export()} is {@code final}.
 */
public final class MarkdownExporter extends ReportExporter {

    public MarkdownExporter(AuditLog audit) {
        super(audit);
    }

    @Override
    protected String header() {
        return "| customer | amount |\n|---|---|\n";
    }

    @Override
    protected String row(Sale sale) {
        return "| " + sale.customer() + " | " + sale.amount() + " |\n";
    }

    @Override
    protected String extension() {
        return ".md";
    }
}
