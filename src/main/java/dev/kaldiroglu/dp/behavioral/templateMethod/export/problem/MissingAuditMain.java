package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;

import static dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.SampleReport.DENIZ;
import static dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.SampleReport.REPORT;

/**
 * Stage three with the new Markdown format. The Markdown file is correct, but the audit log
 * has only the CSV export: {@code MarkdownExport} never calls {@code recordAudit}.
 */
public final class MissingAuditMain {

    public static void main(String[] args) {
        AuditLog audit = new AuditLog();
        new CsvExport(audit).export(DENIZ, REPORT);
        System.out.print("Stage three, the Markdown file:\n"
                + new MarkdownExport(audit).export(DENIZ, REPORT).content());
        System.out.println("Stage three, a CSV and a Markdown export, audit log: " + audit.lines());
        System.out.println("The Markdown export is missing: MarkdownExport never calls recordAudit.");
    }
}
