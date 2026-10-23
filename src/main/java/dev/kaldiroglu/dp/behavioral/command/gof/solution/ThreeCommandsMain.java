package dev.kaldiroglu.dp.behavioral.command.gof.solution;

import dev.kaldiroglu.dp.behavioral.command.gof.Application;
import dev.kaldiroglu.dp.behavioral.command.gof.Document;

/**
 * A command that does real work, OpenCommand, and one that combines others, MacroCommand.
 * PasteCommand, which only forwards, is in {@link PasteCommandMain}.
 */
public final class ThreeCommandsMain {

    public static void main(String[] args) {
        Application application = new Application();
        new MenuItem("Open", new OpenCommand(application, () -> "report")).clicked();
        Document opened = application.current();
        System.out.println("Open clicked. Opened '" + opened.name() + "': " + opened.isOpen());

        Document report = SampleReport.helloCopied();
        MacroCommand pasteTwice = new MacroCommand()
                .add(new PasteCommand(report))
                .add(new PasteCommand(report));
        new MenuItem("Paste twice", pasteTwice).clicked();
        System.out.println("A macro of " + pasteTwice.size() + " commands clicked. The report reads: "
                + report.text());
    }
}
