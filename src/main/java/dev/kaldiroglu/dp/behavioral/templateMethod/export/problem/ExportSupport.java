package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Export;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.ExportNotAllowedException;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User;

/**
 * Stage three: <b>a base class with the shared steps, and each format writes its own
 * {@code export()}.</b>
 * <p>
 * The best of the three. No step is copied: the permission check and the audit line are
 * written once, here. And any team can add a format by writing a subclass; nothing in
 * this file changes.
 * <p>
 * What it does not share is the order. Each subclass writes {@code export()} itself and
 * decides which helpers to call and when. {@link MarkdownExport} was written later, by
 * someone new, and it never calls {@link #recordAudit}. The auditors do not see its
 * exports. The steps are shared; the algorithm is not.
 */
public abstract class ExportSupport {

    private final AuditLog audit;

    protected ExportSupport(AuditLog audit) {
        this.audit = audit;
    }

    public abstract Export export(User user, Report report);

    protected void checkPermission(User user) {
        if (!user.mayExport()) {
            throw new ExportNotAllowedException(user);
        }
    }

    protected void recordAudit(User user, Export export, Report report) {
        audit.record(user, export, report.sales().size());
    }
}
