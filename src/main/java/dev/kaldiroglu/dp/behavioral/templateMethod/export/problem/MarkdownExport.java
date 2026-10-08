package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Export;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User;

/**
 * Stage three: Markdown, added later by a new team member.
 * <p>
 * It compiles, it makes a correct file, and it checks the permission. It never calls
 * {@code recordAudit}, so its exports are missing from the audit log. Nothing in the
 * design could stop this: {@code export()} is the subclass's to write.
 */
public final class MarkdownExport extends ExportSupport {

    public MarkdownExport(AuditLog audit) {
        super(audit);
    }

    @Override
    public Export export(User user, Report report) {
        checkPermission(user);
        StringBuilder content = new StringBuilder("| customer | amount |\n|---|---|\n");
        for (Sale sale : report.sales()) {
            content.append("| ").append(sale.customer()).append(" | ")
                    .append(sale.amount()).append(" |\n");
        }
        return new Export(report.title() + ".md", content.toString());
    }
}
