package dev.kaldiroglu.dp.behavioral.templateMethod.export.solution;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Export;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.CsvExport;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.MarkdownExport;

import java.util.List;

/** Runs stage three and the solution on the same report, and prints both audit logs. */
public final class Main {

    public static void main(String[] args) {
        Report report = new Report("sales-october", List.of(
                new Sale("Ayse", 1200), new Sale("Deniz", 800)));
        User deniz = new User("Deniz", true);

        AuditLog before = new AuditLog();
        new CsvExport(before).export(deniz, report);
        new MarkdownExport(before).export(deniz, report);
        System.out.println("Stage three, two exports, audit log: " + before.lines());

        AuditLog after = new AuditLog();
        for (ReportExporter exporter : List.of(new CsvExporter(after), new HtmlExporter(after),
                new MarkdownExporter(after))) {
            Export export = exporter.export(deniz, report);
            System.out.println("--- " + export.fileName());
            System.out.print(export.content());
        }
        System.out.println("Template method, three exports, audit log: " + after.lines());
    }
}
