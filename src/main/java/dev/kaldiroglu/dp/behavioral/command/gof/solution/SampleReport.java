package dev.kaldiroglu.dp.behavioral.command.gof.solution;

import dev.kaldiroglu.dp.behavioral.command.gof.Application;
import dev.kaldiroglu.dp.behavioral.command.gof.Document;

/** A report opened through a menu item, with "Hello" typed and copied. */
final class SampleReport {

    private SampleReport() {
    }

    static Document helloCopied() {
        Application application = new Application();
        new MenuItem("Open", new OpenCommand(application, () -> "report")).clicked();
        Document report = application.current();
        report.type("Hello");
        report.copy();
        return report;
    }
}
