package dev.kaldiroglu.dp.behavioral.command.gof.solution;

import dev.kaldiroglu.dp.behavioral.command.gof.Document;

/** A menu item holds a PasteCommand and calls execute() when it is clicked. */
public final class PasteCommandMain {

    public static void main(String[] args) {
        Document report = SampleReport.helloCopied();
        new MenuItem("Paste", new PasteCommand(report)).clicked();
        System.out.println("Paste clicked. The report reads: " + report.text());
    }
}
