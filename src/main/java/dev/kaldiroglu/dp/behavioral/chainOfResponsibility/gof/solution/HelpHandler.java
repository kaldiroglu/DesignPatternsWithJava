package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution;

/**
 * The <b>Handler</b>: GoF's {@code HelpHandler}. It has a help topic or not, and a
 * successor.
 * <p>
 * If it has a topic, it shows the help; if not, it passes the request to its successor.
 * The successor is a field of its own, as in GoF's sample code: GoF implementation issue 1
 * (implementing the successor chain), "define new links". Package {@code gof.parent} shows
 * the other choice, "use existing links": the widget's parent field is the next link.
 */
public abstract class HelpHandler {

    private final HelpHandler successor;
    private final String topic;

    protected HelpHandler(HelpHandler successor, String topic) {
        this.successor = successor;
        this.topic = topic;
    }

    public boolean hasHelp() {
        return topic != null;
    }

    public String handleHelp() {
        if (hasHelp()) {
            return "Help: " + topic;
        }
        if (successor != null) {
            return successor.handleHelp();
        }
        return "No help is available.";
    }
}
