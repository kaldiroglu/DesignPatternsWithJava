package dev.kaldiroglu.dp.behavioral.command.gof.solution;

import dev.kaldiroglu.dp.behavioral.command.gof.Document;

/**
 * GoF's template as a method reference: "Paste again" is a SimpleCommand. A menu refuses a
 * label it does not have.
 */
public final class SimpleCommandMain {

    public static void main(String[] args) {
        Document report = SampleReport.helloCopied();
        Menu menu = new Menu()
                .add(new MenuItem("Paste", new PasteCommand(report)))
                .add(new MenuItem("Paste again", new SimpleCommand<>(report, Document::paste)));
        menu.click("Paste");
        menu.click("Paste again");
        System.out.println("Menu " + menu.labels() + " clicked. The report reads: " + report.text());

        try {
            menu.click("paste");
        } catch (IllegalArgumentException e) {
            System.out.println("A label the menu does not have is refused: " + e.getMessage());
        }
    }
}
