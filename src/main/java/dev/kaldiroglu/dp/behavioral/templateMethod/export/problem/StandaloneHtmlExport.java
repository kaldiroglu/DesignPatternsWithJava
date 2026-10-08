package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Export;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.ExportNotAllowedException;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User;

/** Stage one, second copy. Compare with {@link StandaloneCsvExport}: only the text is different. */
public final class StandaloneHtmlExport {

    private final AuditLog audit;

    public StandaloneHtmlExport(AuditLog audit) {
        this.audit = audit;
    }

    public Export export(User user, Report report) {
        if (!user.mayExport()) {
            throw new ExportNotAllowedException(user);
        }
        StringBuilder content = new StringBuilder("<table>\n<tr><th>customer</th><th>amount</th></tr>\n");
        for (Sale sale : report.sales()) {
            content.append("<tr><td>").append(sale.customer()).append("</td><td>")
                    .append(sale.amount()).append("</td></tr>\n");
        }
        content.append("</table>\n");
        Export export = new Export(report.title() + ".html", content.toString());
        audit.record(user, export, report.sales().size());
        return export;
    }
}
