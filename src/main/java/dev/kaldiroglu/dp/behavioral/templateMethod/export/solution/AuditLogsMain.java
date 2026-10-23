package dev.kaldiroglu.dp.behavioral.templateMethod.export.solution;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.CsvExport;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.MarkdownExport;

import static dev.kaldiroglu.dp.behavioral.templateMethod.export.solution.SampleReport.DENIZ;
import static dev.kaldiroglu.dp.behavioral.templateMethod.export.solution.SampleReport.REPORT;

/** Runs stage three and the template method on the same report, and prints both audit logs. */
public final class AuditLogsMain {

    public static void main(String[] args) {
        AuditLog before = new AuditLog();
        new CsvExport(before).export(DENIZ, REPORT);
        new MarkdownExport(before).export(DENIZ, REPORT);
        System.out.println("Stage three, two exports, audit log: " + before.lines());

        AuditLog after = new AuditLog();
        new CsvExporter(after).export(DENIZ, REPORT);
        new HtmlExporter(after).export(DENIZ, REPORT);
        new MarkdownExporter(after).export(DENIZ, REPORT);
        System.out.println("Template method, three exports, audit log: " + after.lines());
    }
}
