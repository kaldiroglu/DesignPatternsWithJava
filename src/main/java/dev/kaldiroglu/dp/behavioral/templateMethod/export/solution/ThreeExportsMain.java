package dev.kaldiroglu.dp.behavioral.templateMethod.export.solution;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Export;

import java.util.List;

import static dev.kaldiroglu.dp.behavioral.templateMethod.export.solution.SampleReport.DENIZ;
import static dev.kaldiroglu.dp.behavioral.templateMethod.export.solution.SampleReport.REPORT;

/** Exports the report in the three formats and prints each file. */
public final class ThreeExportsMain {

    public static void main(String[] args) {
        AuditLog audit = new AuditLog();
        for (ReportExporter exporter : List.of(new CsvExporter(audit), new HtmlExporter(audit),
                new MarkdownExporter(audit))) {
            Export export = exporter.export(DENIZ, REPORT);
            System.out.println("--- " + export.fileName());
            System.out.print(export.content());
        }
    }
}
