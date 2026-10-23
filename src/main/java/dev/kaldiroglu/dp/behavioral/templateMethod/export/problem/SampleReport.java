package dev.kaldiroglu.dp.behavioral.templateMethod.export.problem;

import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Report;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.Sale;
import dev.kaldiroglu.dp.behavioral.templateMethod.export.domain.User;

import java.util.List;

/** The report and the user that every run in this package exports with. */
final class SampleReport {

    static final Report REPORT = new Report("sales-october", List.of(
            new Sale("Ayse", 1200), new Sale("Deniz", 800)));
    static final User DENIZ = new User("Deniz", true);

    private SampleReport() {
    }
}
