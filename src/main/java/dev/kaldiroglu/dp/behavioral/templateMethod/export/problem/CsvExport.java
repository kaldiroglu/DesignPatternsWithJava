package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Export;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User;

/** Stage three: CSV, with the steps called in the right order. */
public final class CsvExport extends ExportSupport {

    public CsvExport(AuditLog audit) {
        super(audit);
    }

    @Override
    public Export export(User user, Report report) {
        checkPermission(user);
        StringBuilder content = new StringBuilder("customer,amount\n");
        for (Sale sale : report.sales()) {
            content.append(sale.customer()).append(',').append(sale.amount()).append('\n');
        }
        Export export = new Export(report.title() + ".csv", content.toString());
        recordAudit(user, export, report);
        return export;
    }
}
