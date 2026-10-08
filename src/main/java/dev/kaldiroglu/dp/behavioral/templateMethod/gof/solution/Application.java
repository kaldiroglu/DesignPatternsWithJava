package dev.kaldiroglu.dp.behavioral.templateMethod.gof.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * The <b>AbstractClass</b>: GoF's {@code Application}.
 * <p>
 * {@link #openDocument} is the <b>template method</b>. It fixes the order of
 * the steps and calls three kinds of operation:
 * <ul>
 *   <li>a primitive operation that a subclass must write: {@link #canOpenDocument};</li>
 *   <li>a factory method that a subclass must write: {@link #doCreateDocument};</li>
 *   <li>a hook that a subclass may write: {@link #aboutToOpenDocument}, which does nothing
 *       here.</li>
 * </ul>
 * It is {@code final}, so an application can change a step but not the order of the steps.
 */
public abstract class Application {

    private final List<Document> documents = new ArrayList<>();
    private final List<String> events = new ArrayList<>();

    /** The template method. */
    public final void openDocument(String name) {
        if (!canOpenDocument(name)) {
            return;
        }
        Document document = doCreateDocument(name);
        if (document != null) {
            documents.add(document);
            aboutToOpenDocument(document);
            document.open();
            events.add("open " + name);
            document.doRead(events);
        }
    }

    /** A primitive operation: can this application open this file? */
    protected abstract boolean canOpenDocument(String name);

    /** A factory method: which kind of document this application creates. */
    protected abstract Document doCreateDocument(String name);

    /** A hook: called just before a document is opened. Does nothing by default. */
    protected void aboutToOpenDocument(Document document) {
    }

    protected void record(String event) {
        events.add(event);
    }

    public List<Document> documents() {
        return List.copyOf(documents);
    }

    public List<String> events() {
        return List.copyOf(events);
    }
}
