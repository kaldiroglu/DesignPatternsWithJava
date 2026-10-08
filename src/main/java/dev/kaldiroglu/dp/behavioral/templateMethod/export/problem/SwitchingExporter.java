package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Export;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.ExportNotAllowedException;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User;

/**
 * Stage two: <b>one class, and a switch on the format inside the steps that differ.</b>
 * <p>
 * A real improvement on stage one. The algorithm is written once: the permission check
 * comes first and the audit line comes last, for every format.
 * <p>
 * What it costs: every format lives in this class. A new format is a new constant and a
 * branch in three switches, and a team that owns its own format cannot add it without
 * editing this file.
 */
public final class SwitchingExporter {

    private final AuditLog audit;

    public SwitchingExporter(AuditLog audit) {
        this.audit = audit;
    }

    public Export export(User user, Report report, Format format) {
        if (!user.mayExport()) {
            throw new ExportNotAllowedException(user);
        }
        StringBuilder content = new StringBuilder(switch (format) {
            case CSV -> "customer,amount\n";
            case HTML -> "<table>\n<tr><th>customer</th><th>amount</th></tr>\n";
        });
        for (Sale sale : report.sales()) {
            content.append(switch (format) {
                case CSV -> sale.customer() + "," + sale.amount() + "\n";
                case HTML -> "<tr><td>" + sale.customer() + "</td><td>" + sale.amount() + "</td></tr>\n";
            });
        }
        if (format == Format.HTML) {
            content.append("</table>\n");
        }
        String extension = switch (format) {
            case CSV -> ".csv";
            case HTML -> ".html";
        };
        Export export = new Export(report.title() + extension, content.toString());
        audit.record(user, export, report.sales().size());
        return export;
    }
}
