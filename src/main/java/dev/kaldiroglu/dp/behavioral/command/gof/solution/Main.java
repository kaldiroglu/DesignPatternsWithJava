package dev.kaldiroglu.dp.behavioral.command.gof.solution;

import dev.kaldiroglu.dp.behavioral.command.gof.Application;
import dev.kaldiroglu.dp.behavioral.command.gof.Document;

/** Shows menu items that only execute a command: open, paste, a method reference and a macro. */
public class Main {

    public static void main(String[] args) {
        Application application = new Application();
        new MenuItem("Open", new OpenCommand(application, () -> "report")).clicked();
        Document report = application.current();
        System.out.println("Open clicked. Opened '" + report.name() + "': " + report.isOpen());

        report.type("Hello");
        report.copy();
        Menu menu = new Menu()
                .add(new MenuItem("Paste", new PasteCommand(report)))
                .add(new MenuItem("Paste again", new SimpleCommand<>(report, Document::paste)));
        menu.click("Paste");
        menu.click("Paste again");
        System.out.println("Menu " + menu.labels() + " clicked. The report reads: " + report.text());

        MacroCommand pasteTwice = new MacroCommand()
                .add(new PasteCommand(report))
                .add(new PasteCommand(report));
        new MenuItem("Paste twice", pasteTwice).clicked();
        System.out.println("A macro of " + pasteTwice.size() + " commands clicked. The report reads: "
                + report.text());

        try {
            menu.click("paste");
        } catch (IllegalArgumentException e) {
            System.out.println("A label the menu does not have is refused: " + e.getMessage());
        }
    }
}
