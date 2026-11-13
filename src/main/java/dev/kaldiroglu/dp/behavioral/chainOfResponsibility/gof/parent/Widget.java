package dev.kaldiroglu.dp.behavioral.chainOfResponsibility.gof.parent;

/**
 * A control on the screen: a window, a dialog or a button.
 * <p>
 * Every widget keeps its parent, the widget that holds it. {@link #path()} uses the parent
 * to say where the widget is. {@link #handleHelp()} uses the same parent as the next link
 * of the help chain. No field is added for the chain.
 */
public final class Widget {

    private final Widget parent;
    private final String name;
    private final String topic;

    public Widget(Widget parent, String name, String topic) {
        this.parent = parent;
        this.name = name;
        this.topic = topic;
    }

    public Widget(Widget parent, String name) {
        this(parent, name, null);
    }

    public String path() {
        if (parent == null)
            return name;
        return parent.path() + " > " + name;
    }

    public String handleHelp() {
        if (topic != null)
            return "Help: " + topic;
        if (parent != null)
            return parent.handleHelp();
        return "No help is available.";
    }
}
