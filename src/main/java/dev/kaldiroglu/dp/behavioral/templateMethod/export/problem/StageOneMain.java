package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.AuditLog;

import static dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.SampleReport.DENIZ;
import static dev.kaldiroglu.dp.behavioral.templateMethod.export.problem.SampleReport.REPORT;

/** Stage one: each format has its own copy of the algorithm. Both exports are audited. */
public final class StageOneMain {

    public static void main(String[] args) {
        AuditLog audit = new AuditLog();
        new StandaloneCsvExport(audit).export(DENIZ, REPORT);
        new StandaloneHtmlExport(audit).export(DENIZ, REPORT);
        System.out.println("Stage one, a copy per format, audit log: " + audit.lines());
    }
}
