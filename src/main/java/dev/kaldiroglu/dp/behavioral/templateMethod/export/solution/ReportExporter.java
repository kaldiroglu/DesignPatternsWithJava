package dev.kaldiroglu.dp.behavioral.templateMethod.export.solution;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Export;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.ExportNotAllowedException;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User;

/**
 * The <b>AbstractClass</b>: the export algorithm, written once.
 * <p>
 * {@link #export} is the <b>template method</b>. It is {@code final}, so no subclass can
 * change the order of the steps or leave one out: the permission check is always first and
 * the audit line is always last. Compare {@code problem.ExportSupport}, where each
 * subclass wrote {@code export()} itself.
 * <p>
 * The steps that differ by format are {@code protected abstract}: {@link #header},
 * {@link #row} and {@link #extension}. A subclass must write them. {@link #footer} is a
 * <b>hook</b>: it does nothing here, and a subclass may override it if it needs to.
 * <p>
 * The subclass never calls the steps; the template method calls them. GoF call this the
 * Hollywood principle: "don't call us, we'll call you".
 */
public abstract class ReportExporter {

    private final AuditLog audit;

    protected ReportExporter(AuditLog audit) {
        this.audit = audit;
    }

    /** The template method. */
    public final Export export(User user, Report report) {
        if (!user.mayExport()) {
            throw new ExportNotAllowedException(user);
        }
        StringBuilder content = new StringBuilder(header());
        for (Sale sale : report.sales()) {
            content.append(row(sale));
        }
        content.append(footer());
        Export export = new Export(report.title() + extension(), content.toString());
        audit.record(user, export, report.sales().size());
        return export;
    }

    /** A primitive operation: every format must write its header. */
    protected abstract String header();

    /** A primitive operation: one line of the file for one sale. */
    protected abstract String row(Sale sale);

    /** A primitive operation: ".csv", ".html", ... */
    protected abstract String extension();

    /** A hook: most formats need nothing after the rows. */
    protected String footer() {
        return "";
    }
}
