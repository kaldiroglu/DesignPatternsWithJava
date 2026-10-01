package dev.kaldiroglu.dp.behavioral.command.gof.solution;

import dev.kaldiroglu.dp.behavioral.command.gof.Application;
import dev.kaldiroglu.dp.behavioral.command.gof.Document;

import java.util.function.Supplier;

/**
 * A <b>ConcreteCommand</b> that does more than forward: GoF's {@code OpenCommand}.
 * <p>
 * It asks the user for a name, creates a document, adds it to the application and opens
 * it. Several steps and a conversation with the user — so a command can be as clever as
 * the request needs. How clever it should be is GoF implementation issue 1 (how
 * intelligent should a command be?): somewhere between forwarding to a receiver and doing
 * the whole job itself.
 */
public final class OpenCommand implements Command {

    private final Application application;
    private final Supplier<String> askUser;   // stands in for a file dialog

    public OpenCommand(Application application, Supplier<String> askUser) {
        this.application = application;
        this.askUser = askUser;
    }

    @Override
    public void execute() {
        String name = askUser.get();
        if (name == null || name.isBlank()) {
            return;                            // the user cancelled the dialog
        }
        Document document = new Document(name, application.clipboard());
        application.add(document);
        document.open();
    }
}
