package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;

import static dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.SampleReport.DENIZ;
import static dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.SampleReport.MERT;
import static dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.SampleReport.REPORT;

/**
 * Stage two: one class switches on the format. Both of Deniz's exports are audited; Mert,
 * who may not export, is refused and nothing is audited.
 */
public final class StageTwoMain {

    public static void main(String[] args) {
        AuditLog audit = new AuditLog();
        SwitchingExporter exporter = new SwitchingExporter(audit);
        exporter.export(DENIZ, REPORT, Format.CSV);
        exporter.export(DENIZ, REPORT, Format.HTML);
        System.out.println("Stage two, Mert asks for a CSV file: "
                + SampleReport.refusal(() -> exporter.export(MERT, REPORT, Format.CSV)));
        System.out.println("Stage two, Mert asks for an HTML file: "
                + SampleReport.refusal(() -> exporter.export(MERT, REPORT, Format.HTML)));
        System.out.println("Stage two, one class with a switch, audit log: " + audit.lines());
    }
}
