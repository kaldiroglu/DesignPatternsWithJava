package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User;

import java.util.List;

/**
 * Runs the three stages on the same report. The first two audit every export; in stage
 * three the Markdown export never writes its audit line.
 */
public final class Main {

    public static void main(String[] args) {
        Report report = new Report("sales-october", List.of(
                new Sale("Ayse", 1200), new Sale("Deniz", 800)));
        User deniz = new User("Deniz", true);

        AuditLog one = new AuditLog();
        new StandaloneCsvExport(one).export(deniz, report);
        new StandaloneHtmlExport(one).export(deniz, report);
        System.out.println("Stage one, a copy per format, audit log: " + one.lines());

        AuditLog two = new AuditLog();
        SwitchingExporter exporter = new SwitchingExporter(two);
        exporter.export(deniz, report, Format.CSV);
        exporter.export(deniz, report, Format.HTML);
        System.out.println("Stage two, one class with a switch, audit log: " + two.lines());

        AuditLog three = new AuditLog();
        new CsvExport(three).export(deniz, report);
        System.out.print("Stage three, the Markdown file:\n"
                + new MarkdownExport(three).export(deniz, report).content());
        System.out.println("Stage three, a CSV and a Markdown export, audit log: " + three.lines());
        System.out.println("The Markdown export is missing: MarkdownExport never calls recordAudit.");
    }
}
