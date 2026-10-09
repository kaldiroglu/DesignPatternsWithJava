package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.solution;

/**
 * The <b>Handler</b>: GoF's {@code HelpHandler}. It has a help topic or not, and a
 * successor.
 * <p>
 * If it has a topic, it shows the help; if not, it passes the request to its successor.
 * GoF implementation issue 1 (implementing the successor chain): here the chain is not a
 * new set of links — each widget's parent is its successor, so the chain is the window's
 * own containment.
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
