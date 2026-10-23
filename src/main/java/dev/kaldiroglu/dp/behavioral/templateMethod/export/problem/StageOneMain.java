package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;

import static dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.SampleReport.DENIZ;
import static dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.SampleReport.MERT;
import static dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.SampleReport.REPORT;

/**
 * Stage one: each format has its own copy of the algorithm. Both of Deniz's exports are
 * audited; Mert, who may not export, is refused in both copies and nothing is audited.
 */
public final class StageOneMain {

    public static void main(String[] args) {
        AuditLog audit = new AuditLog();
        new StandaloneCsvExport(audit).export(DENIZ, REPORT);
        new StandaloneHtmlExport(audit).export(DENIZ, REPORT);
        System.out.println("Stage one, Mert asks for a CSV file: "
                + SampleReport.refusal(() -> new StandaloneCsvExport(audit).export(MERT, REPORT)));
        System.out.println("Stage one, Mert asks for an HTML file: "
                + SampleReport.refusal(() -> new StandaloneHtmlExport(audit).export(MERT, REPORT)));
        System.out.println("Stage one, a copy per format, audit log: " + audit.lines());
    }
}
