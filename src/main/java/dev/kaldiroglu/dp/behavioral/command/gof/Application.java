package dev.kaldiroglu.dp.behavioral.command.gof;

import java.util.ArrayList;
import java.util.List;

/**
 * GoF's {@code Application}: the other <b>Receiver</b>, and the class that knows which
 * documents are open.
 * <p>
 * {@code OpenCommand} acts on this rather than on a document, because opening a document
 * means creating one and adding it here.
 */
public final class Application {

    private final Clipboard clipboard = new Clipboard();
    private final List<Document> documents = new ArrayList<>();

    public Clipboard clipboard() {
        return clipboard;
    }

    public void add(Document document) {
        documents.add(document);
    }

    public List<Document> documents() {
        return List.copyOf(documents);
    }

    /** The document most recently opened: the one the user is looking at. */
    public Document current() {
        return documents.getLast();
    }
}
