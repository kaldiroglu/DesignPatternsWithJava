package dev.kaldiroglu.dp.behavioral.command.gof.solution;

import dev.kaldiroglu.dp.behavioral.command.gof.Document;

/**
 * A <b>ConcreteCommand</b>: GoF's {@code PasteCommand}, whose "receiver is the Document
 * object it is supplied upon instantiation" (p. 234).
 * <p>
 * It binds a receiver to an action and does nothing else. The work is the document's.
 */
public final class PasteCommand implements Command {

    private final Document document;

    public PasteCommand(Document document) {
        this.document = document;
    }

    @Override
    public void execute() {
        document.paste();
    }
}
