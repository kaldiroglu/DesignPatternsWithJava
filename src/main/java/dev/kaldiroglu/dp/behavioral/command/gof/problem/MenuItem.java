package dev.kaldiroglu.dp.behavioral.command.gof.problem;

import dev.kaldiroglu.dp.behavioral.command.gof.Application;
import dev.kaldiroglu.dp.behavioral.command.gof.Document;

import java.util.function.Supplier;

/**
 * A menu item that knows what it does.
 * <p>
 * This is the design GoF's motivation says a toolkit cannot have: "the toolkit can't
 * implement the request explicitly in the button or menu, because only applications that
 * use the toolkit know what should be done on which object" (p. 233). Here it does
 * anyway, so the toolkit's menu item imports the application's classes, branches on its
 * own label, and has to be edited for every menu entry any application will ever add.
 * <p>
 * The label is a string, so a menu entry spelled differently from its branch compiles and
 * fails the first time somebody clicks it.
 */
public final class MenuItem {

    private final String label;
    private final Application application;    // the toolkit now knows the application
    private final Supplier<String> askUser;   // stands in for a file dialog

    public MenuItem(String label, Application application, Supplier<String> askUser) {
        this.label = label;
        this.application = application;
        this.askUser = askUser;
    }

    public String label() {
        return label;
    }

    public void clicked() {
        switch (label) {
            case "Open" -> {
                Document document = new Document(askUser.get(), application.clipboard());
                application.add(document);
                document.open();
            }
            case "Copy" -> application.current().copy();
            case "Paste" -> application.current().paste();
            default -> throw new IllegalStateException("no such menu entry: " + label);
        }
    }
}
