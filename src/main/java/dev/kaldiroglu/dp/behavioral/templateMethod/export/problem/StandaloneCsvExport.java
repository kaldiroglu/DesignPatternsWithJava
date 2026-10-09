package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Export;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.ExportNotAllowedException;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User;

/**
 * Stage one: <b>each format has its own copy of the whole algorithm.</b>
 * <p>
 * Check the user, build the header, build a line per sale, name the file, write the audit
 * record. {@link StandaloneHtmlExport} does the same five things in the same order. Three
 * of them differ: the header, the lines and the file extension.
 * <p>
 * It works. What it costs: the other two steps, the permission check and the audit line,
 * are the same in every copy, so a fix to either must be made in every class, and a missed
 * copy is a silent difference.
 */
public final class StandaloneCsvExport {

    private final AuditLog audit;

    public StandaloneCsvExport(AuditLog audit) {
        this.audit = audit;
    }

    public Export export(User user, Report report) {
        if (!user.mayExport()) {
            throw new ExportNotAllowedException(user);
        }
        StringBuilder content = new StringBuilder("customer,amount\n");
        for (Sale sale : report.sales()) {
            content.append(sale.customer()).append(',').append(sale.amount()).append('\n');
        }
        Export export = new Export(report.title() + ".csv", content.toString());
        audit.record(user, export, report.sales().size());
        return export;
    }
}
