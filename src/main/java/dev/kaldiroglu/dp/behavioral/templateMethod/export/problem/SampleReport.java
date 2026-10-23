package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.ExportNotAllowedException;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User;

import java.util.List;

/**
 * The report that every run in this package exports, and two users: Deniz may export it,
 * Mert may not.
 */
final class SampleReport {

    static final Report REPORT = new Report("sales-october", List.of(
            new Sale("Ayse", 1200), new Sale("Deniz", 800)));
    static final User DENIZ = new User("Deniz", true);
    static final User MERT = new User("Mert", false);

    private SampleReport() {
    }

    /** Runs an export that should be refused, and returns the reason it was refused. */
    static String refusal(Runnable export) {
        try {
            export.run();
            return "exported";
        } catch (ExportNotAllowedException e) {
            return e.getMessage();
        }
    }
}
